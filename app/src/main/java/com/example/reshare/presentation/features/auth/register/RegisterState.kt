package com.example.reshare.presentation.features.auth.register

data class RegisterState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String = ""
)
