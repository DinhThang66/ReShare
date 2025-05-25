package com.example.reshare.presentation.features.mainGraph.home.itemDetail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.reshare.R
import com.example.reshare.ui.theme.BlueD
import com.example.reshare.ui.theme.DarkGreen
import com.example.reshare.ui.theme.DarkPurple
import com.example.reshare.ui.theme.DarkYellow
import com.example.reshare.ui.theme.LightGreen
import com.example.reshare.ui.theme.MilkM
import com.example.reshare.ui.theme.OrangeM
import com.example.reshare.ui.theme.YellowD
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Potato, carrot & swede mash",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        @Suppress("DEPRECATION")
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.shadow(2.dp)
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            ImageWithBadge(
                imageResId = R.drawable.img,
                badge = BadgeType.Wanted
            )
            ImageActionRow(onWatchlistClick = {})

            PostHeaderInfo(
                avatarRes = R.drawable.img,
                rating = "4.7",
                giverName = "Carol",
                title = "Potato, carrot & swede mash",
                timeAgo = "7 minutes ago",
                roleLabel = "Volunteer",
                roleColor = Color(0xFF6A1B9A),
                description = "I would appreciate if can get a/some BriaMax water fitter(s) for my jug."
            )

            Spacer(modifier = Modifier.height(16.dp))
            PickupInfoSection(
                pickupTimes = "Pick up by 7pm today or 10 to 12pm tomorrow.",
                pickupInstructions = "Please message your pick up time"
            )

            Spacer(modifier = Modifier.height(16.dp))
            LocationMapSection(
                location = LatLng(51.5014, -0.1419),
                distanceText = "1.1 km away",
                radiusMeters = 300.0
            )

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(50)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkPurple,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "Send a message",
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}





data class BadgeStyle(
    val text: String,
    val textColor: Color,
    val backgroundColor: Color
)
// Badge default
object BadgeType {
    val New = BadgeStyle("New", DarkGreen, LightGreen)
    val Reduced = BadgeStyle("Reduced", BlueD, YellowD)
    val Wanted = BadgeStyle("Wanted", OrangeM, MilkM)
}

@Composable
fun ImageWithBadge(
    imageResId: Int,
    badge: BadgeStyle = BadgeType.New
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Product image",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = badge.text,
            color = badge.textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(badge.backgroundColor, shape = CircleShape)
                .padding(horizontal = 10.dp)
                .align(Alignment.TopStart)
        )
    }
}

@Composable
fun ImageActionRow(
    onWatchlistClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.3f))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        IconWithLabel(
            icon = Icons.Default.Star,
            label = "Watchlist",
            onClick = onWatchlistClick
        )
    }
}

@Composable
fun IconWithLabel(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PostHeaderInfo(
    avatarRes: Int,
    rating: String,
    giverName: String,
    title: String,
    timeAgo: String,
    roleLabel: String,
    roleColor: Color,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier
            .size(48.dp)
            .offset(y = (-10).dp)) {
            Image(
                painter = painterResource(id = avatarRes),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            // Rating badge
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 6.dp)
                    .background(DarkYellow, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "⭐ $rating",
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "$giverName is giving away",
                fontSize = 14.sp
            )
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.Gray
                )
                Text(
                    text = "Added $timeAgo",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(" • ", color = Color.Gray)
                Text(
                    text = roleLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = roleColor
                )
            }
        }
    }
    Text(
        text = description,
        fontSize = 15.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    )
}

@Composable
fun PickupInfoSection(
    pickupTimes: String,
    pickupInstructions: String
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Pick–up times",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = pickupTimes,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
        )

        Text(
            text = "Pick–up instructions",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = pickupInstructions,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun LocationMapSection(
    location: LatLng,
    distanceText: String,
    radiusMeters: Double = 300.0,
) {
    val context = LocalContext.current

    var showDirections by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "LOCATION",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.Gray
                )
                Text(
                    text = distanceText,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = false)
            ) {
                Marker(state = MarkerState(position = location))

                Circle(
                    center = location,
                    radius = radiusMeters,
                    fillColor = Color(0x443F0071), // semi-transparent fill
                    strokeColor = Color(0xFF3F0071),
                    strokeWidth = 2f
                )
            }

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val size = this.size
                            val center = Offset(x = size.width / 2f, y = size.height / 2f)
                            val radius = 300f

                            val distance = (offset - center).getDistance()
                            showDirections = distance <= radius
                        }
                    }
            )

            if (showDirections) {
                IconButton(
                    onClick = {
                        // Intent mở Google Maps tại vị trí location
                        val gmmIntentUri = Uri.parse("google.navigation:q=${location.latitude},${location.longitude}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                        context.startActivity(mapIntent)
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.BottomEnd)
                        .background(Color.White, shape = CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ggmap),
                        contentDescription = "Directions",
                        modifier = Modifier
                            .size(28.dp)
                    )
                }
            }
        }
    }
}
