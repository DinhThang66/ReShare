package com.example.reshare.presentation.features.mainGraph.home.itemDetail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.reshare.R
import com.example.reshare.domain.model.Product
import com.example.reshare.presentation.utils.Screen
import com.example.reshare.presentation.utils.formatTimeAgo
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

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    navController: NavController,
    viewModel: ItemDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val product = state.product

    // Set product
    LaunchedEffect(Unit) {
        val savedProduct = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Product>("product")
        savedProduct?.let {
            viewModel.onEvent(ItemDetailUiEvent.SetProduct(it))
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.remove<Product>("product")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = product?.title?:"",
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
            product?.let {
                ImageWithBadge(
                    image = product.images[0],
                    tag = it.tag
                )
                ImageActionRow(onWatchlistClick = {})

                PostHeaderInfo(
                    avatar = it.createdBy.profilePic,
                    rating = "5.0",
                    userName = it.createdBy.firstName,
                    title = it.title,
                    timeAgo = formatTimeAgo(it.updatedAt),
                    roleLabel = "Personal",
                    roleColor = Color(0xFF6A1B9A),
                    description = it.description,
                    tag = it.tag,
                    onAvatarClick = {
                        navController.navigate(Screen.UserProfile.route + "/${it.createdBy.id}")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                PickupInfoSection(
                    pickupTimes = it.pickupTimes,
                    pickupInstructions = it.pickupInstructions
                )

                Spacer(modifier = Modifier.height(16.dp))
                LocationMapSection(
                    location = LatLng(it.locationLat, it.locationLng),
                    distanceText = String.format("%.1f km away", product.distance / 1000.0),
                    radiusMeters = 300.0
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("product", product)
                    navController.navigate(Screen.MakeARequest.route)
                },
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
    image: String,
    tag: String
) {
    val badge = when(tag) {
        "free" -> BadgeType.New
        "reduced" -> BadgeType.Reduced
        "wanted" -> BadgeType.Wanted
        else -> BadgeType.New
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = image,
            contentDescription = "Product image",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.img),
            error = painterResource(R.drawable.img)
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
    avatar: String,
    rating: String,
    userName: String,
    title: String,
    timeAgo: String,
    roleLabel: String,
    roleColor: Color,
    description: String,
    tag: String,
    onAvatarClick: () -> Unit = {}
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
            AsyncImage(
                model = avatar,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable(onClick = onAvatarClick),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.user),
                error = painterResource(R.drawable.user)
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
            if (tag == "free") {
                Text(
                    text = "$userName is giving away",
                    fontSize = 14.sp
                )
            }else if(tag == "paid") {
                Text(
                    text = "$userName is selling",
                    fontSize = 14.sp
                )
            }
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
    pickupInstructions: String?
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
        if(!pickupInstructions.isNullOrBlank()) {
            Text(
                text = pickupInstructions,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )
        }
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
