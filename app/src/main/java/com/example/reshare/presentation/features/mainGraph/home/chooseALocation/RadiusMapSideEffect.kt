package com.example.reshare.presentation.features.mainGraph.home.chooseALocation

sealed class RadiusMapSideEffect {
    data class ShowError(val message: String) : RadiusMapSideEffect()
    data object CloseScreen : RadiusMapSideEffect()
    data object LocationUpdated : RadiusMapSideEffect()
}