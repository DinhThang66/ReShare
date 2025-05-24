package com.example.reshare.presentation.features.mainGraph.home.chooseALocation

import com.google.android.gms.maps.model.LatLng

sealed class RadiusMapUiEvent {
    data class OnQueryChange(val query: String) : RadiusMapUiEvent()
    data class OnSuggestionSelected(val placeId: String) : RadiusMapUiEvent()
    data class OnRadiusChange(val miles: Float) : RadiusMapUiEvent()
    data class OnSuggestionSelectedWithLatLng(val latLng: LatLng) : RadiusMapUiEvent()

    data object OnApply : RadiusMapUiEvent()
    data object OnClose : RadiusMapUiEvent()
}