package com.example.reshare.presentation.features.mainGraph.home.chooseALocation

sealed class ChooseALocationSideEffect {
    data class ShowError(val message: String) : ChooseALocationSideEffect()
    data object CloseScreen : ChooseALocationSideEffect()
    data object LocationUpdated : ChooseALocationSideEffect()
}