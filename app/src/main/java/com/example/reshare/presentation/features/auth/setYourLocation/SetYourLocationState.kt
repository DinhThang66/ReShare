package com.example.reshare.presentation.features.auth.setYourLocation

import com.example.reshare.domain.model.PlaceSuggestion
import com.google.android.gms.maps.model.LatLng

data class SetYourLocationState (
    val searchQuery: String = "",
    val suggestions: List<PlaceSuggestion> = emptyList(),
    val selectedLocation: LatLng = LatLng(21.0049, 105.8431),
    val zoomLevel: Float = 17f,
    val isRequestingLocation: Boolean = false,

    val isLoading: Boolean = false,
    val error: String = "",
)