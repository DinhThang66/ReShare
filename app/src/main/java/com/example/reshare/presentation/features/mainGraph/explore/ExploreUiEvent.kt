package com.example.reshare.presentation.features.mainGraph.explore

sealed class ExploreUiEvent {
    data object Refresh : ExploreUiEvent()
}