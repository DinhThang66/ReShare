package com.example.reshare.presentation.features.mainGraph.community.userProfile

import com.example.reshare.domain.model.User

data class UserProfileState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,

    val createdChannelId: String? = null
)