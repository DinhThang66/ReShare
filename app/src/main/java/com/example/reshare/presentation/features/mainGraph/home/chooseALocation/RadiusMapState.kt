package com.example.reshare.presentation.features.mainGraph.home.chooseALocation

import com.example.reshare.domain.model.PlaceSuggestion
import com.google.android.gms.maps.model.LatLng

data class RadiusMapState(
    val searchQuery: String = "",
    val suggestions: List<PlaceSuggestion> = emptyList(),
    val selectedLocation: LatLng = LatLng(20.8449, 106.6881),
    val radiusMiles: Float = 12.4f,
    val isLoading: Boolean = false
)