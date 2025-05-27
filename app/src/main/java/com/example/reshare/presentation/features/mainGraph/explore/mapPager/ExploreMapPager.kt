package com.example.reshare.presentation.features.mainGraph.explore.mapPager

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("UnrememberedMutableState")
@Composable
fun ExploreMapPager(modifier: Modifier = Modifier) {
    val location = LatLng(20.8449, 106.6881)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(location),
            title = "Marker in Hai Phong"
        )

        Circle(
            center = location,
            radius = 300.0,
            fillColor = Color(0x443F0071), // semi-transparent fill
            strokeColor = Color(0xFF3F0071),
            strokeWidth = 2f
        )
    }

}