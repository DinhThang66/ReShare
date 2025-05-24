package com.example.reshare.presentation.features.mainGraph.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.reshare.ui.theme.DarkPurple
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlin.math.cos

@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RadiusMapScreen(
    navController: NavController,
    center: LatLng = LatLng(51.5074, -0.1278), // London
) {
    var radiusMiles by remember { mutableFloatStateOf(12.4f) }
    val radiusInMeters = radiusMiles * 1609.34f         // Cập nhật động theo Slider

    val cameraPositionState = rememberCameraPositionState()

    // Automatically align display area
    LaunchedEffect(radiusInMeters) {
        val bounds = calculateBounds(center, radiusInMeters)
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngBounds(bounds, 50) // padding circle
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            LocationSelectorHeader(
                onClose = { navController.popBackStack() }
            )
            GoogleMap(
                modifier = Modifier.fillMaxWidth().height(350.dp),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = center),
                    title = "Luân Đôn"
                )
                Circle(
                    center = center,
                    radius = radiusInMeters.toDouble(),
                    fillColor = Color(0x334285F4), // Semi-transparent purple
                    strokeColor = Color(0xFF4285F4),
                    strokeWidth = 2f
                )
            }

            DistanceSelector(
                value = radiusMiles,
                onValueChange = { radiusMiles = it },
                onApply = {}
            )
        }
    }
}

@Composable
fun LocationSelectorHeader(
    onClose: () -> Unit = {},
    onUseCurrentLocation: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Top Row: Title + Close button
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Choose a location to see\nwhat's available near you",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center).padding(top = 16.dp)
            )
            IconButton(
                onClick = onClose,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search bar
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search here") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF2F2F2)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedContainerColor = Color(0xFFF2F2F2),
                unfocusedContainerColor = Color(0xFFF2F2F2),
                disabledContainerColor = Color(0xFFF2F2F2)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Use current location
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onUseCurrentLocation() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "Use current location",
                tint = DarkPurple
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Or use current location",
                color = DarkPurple,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun DistanceSelector(
    value: Float,
    onValueChange: (Float) -> Unit,
    onApply: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Select a distance",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 0.5f..25f,
                steps = 49,
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = String.format("%.1f km", value),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onApply,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(50), // bo tròn toàn phần
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3F0071),
                contentColor = Color.White
            )
        ) {
            Text("Apply")
        }
    }
}

fun calculateBounds(center: LatLng, radiusMeters: Float): LatLngBounds {
    val lat = center.latitude
    val lng = center.longitude

    val latOffset = radiusMeters / 111000f // ~111km per degree latitude
    val lngOffset = radiusMeters / (111000f * cos(Math.toRadians(lat))) // correct for longitude shrinking

    val southwest = LatLng(lat - latOffset, lng - lngOffset)
    val northeast = LatLng(lat + latOffset, lng + lngOffset)

    return LatLngBounds(southwest, northeast)
}