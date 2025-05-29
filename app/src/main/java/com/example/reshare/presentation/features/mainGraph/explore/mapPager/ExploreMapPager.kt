package com.example.reshare.presentation.features.mainGraph.explore.mapPager

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.reshare.domain.model.Product
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
    products: List<Product>
) {
    val location = LatLng(21.005403, 105.843048)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
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
            center = location,
            radius = 5000.0,
            fillColor = Color(0x443F0071), // semi-transparent fill
            strokeColor = Color(0xFF3F0071),
            strokeWidth = 2f
        )
    }

}