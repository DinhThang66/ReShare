package com.example.reshare.presentation.core

sealed class MainActivityEvent {
    data object ConnectChatUserIfNeeded : MainActivityEvent()
}