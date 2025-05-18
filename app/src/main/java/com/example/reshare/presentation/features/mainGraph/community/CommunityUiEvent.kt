package com.example.reshare.presentation.features.mainGraph.community

sealed class CommunityUiEvent {
    data object Refresh : CommunityUiEvent()
    data object LoadNextPage : CommunityUiEvent()
}