package com.example.reshare.presentation.features.sideBar.myListing

sealed class MyListingsUiEvent {
    data object Refresh : MyListingsUiEvent()
}