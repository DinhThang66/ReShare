package com.example.reshare.presentation.features.mainGraph.home.chooseALocation

import com.google.android.gms.maps.model.LatLng

sealed class ChooseALocationUiEvent {
    data class OnQueryChange(val query: String) : ChooseALocationUiEvent()
    data class OnSuggestionSelected(val placeId: String) : ChooseALocationUiEvent()
    data class OnRadiusChange(val km: Float) : ChooseALocationUiEvent()
    data class OnSuggestionSelectedWithLatLng(
        val latLng: LatLng, val isRequesting: Boolean = false
    ) : ChooseALocationUiEvent()

    data class OnApply(val latLng: LatLng, val km: Float) : ChooseALocationUiEvent()
    data object OnClose : ChooseALocationUiEvent()
}