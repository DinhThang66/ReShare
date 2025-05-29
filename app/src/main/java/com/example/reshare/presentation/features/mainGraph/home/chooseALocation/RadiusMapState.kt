package com.example.reshare.presentation.features.mainGraph.home.chooseALocation

import com.example.reshare.domain.model.PlaceSuggestion
import com.google.android.gms.maps.model.LatLng

data class RadiusMapState(
    val searchQuery: String = "",
    val suggestions: List<PlaceSuggestion> = emptyList(),
    val selectedLocation: LatLng = LatLng(21.005403, 105.843048),
    val radiusMiles: Float = 3f,
    val isLoading: Boolean = false
)