package com.example.reshare.presentation.features.auth.setYourLocation

import com.google.android.gms.maps.model.LatLng

sealed class SetYourLocationUiEvent {
    data class OnQueryChange(val query: String) : SetYourLocationUiEvent()
    data class OnSuggestionSelected(val placeId: String) : SetYourLocationUiEvent()
    data class OnCameraMoved(val latLng: LatLng) : SetYourLocationUiEvent()
    data class OnSuggestionSelectedWithLatLng(val latLng: LatLng, val isRequesting: Boolean = false) : SetYourLocationUiEvent()
    data class OnZoomChanged(val zoom: Float) : SetYourLocationUiEvent()
}