package com.example.reshare.presentation.core

data class MainActivityState (
    val hasValidToken: Boolean? = null,
    val hasLocation: Boolean? = null,
    val isChatReady: Boolean = false
)