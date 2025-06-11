package com.example.reshare.presentation.features.mainGraph.explore.mapPager

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.reshare.domain.model.Product
import com.example.reshare.presentation.utils.calculateBounds
import com.example.reshare.ui.theme.BlueBorder
import com.example.reshare.ui.theme.BlueFill
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("UnrememberedMutableState")
@Composable
fun ExploreMapPager(
    products: List<Product>,
    viewModel: ExploreMapViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val cameraPositionState = rememberCameraPositionState()

    // Initial camera update
    LaunchedEffect(state.selectedLocation, state.radius) {
        val bounds = calculateBounds(state.selectedLocation, state.radius * 1000f)
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngBounds(bounds, 50)
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Vẽ từng marker từ dữ liệu
        products.forEach { product ->
            val latLng = LatLng(
                product.locationLat,
                product.locationLng
            )

            Marker(
                state = MarkerState(position = latLng),
                title = product.title,
                snippet = product.description
            )
        }

        Circle(
            center = state.selectedLocation,
            radius = (state.radius * 1000f).toDouble(),
            fillColor = BlueFill,
            strokeColor = BlueBorder,
            strokeWidth = 2f
        )
    }
}