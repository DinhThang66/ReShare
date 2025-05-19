package com.example.reshare.presentation.features.mainGraph.community.userProfile

sealed class UserProfileUiEvent {
    data class GetUser(val userId: String) : UserProfileUiEvent()
    data class MessageClicked(val userId: String) : UserProfileUiEvent()

    data object ClearNavigation : UserProfileUiEvent()
}