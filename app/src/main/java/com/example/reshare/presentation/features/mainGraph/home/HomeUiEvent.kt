package com.example.reshare.presentation.features.mainGraph.home

sealed class HomeUiEvent {
    data object Refresh : HomeUiEvent()
}