package com.example.reshare.presentation.features.sideBar.myProfile

import com.example.reshare.domain.model.User

data class MyProfileState (
    val user: User = User(
        id = "",
        firstName = "",
        lastName = "",
        email = "",
        profilePic = "",
        latitude = 0.0,
        longitude = 0.0,
        radius = 0f
    ),
)