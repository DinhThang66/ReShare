package com.example.reshare.presentation.features.sideBar.myProfile

sealed class MyProfileUiEvent {
    data object Logout : MyProfileUiEvent()
}