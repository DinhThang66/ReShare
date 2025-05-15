package com.example.reshare.presentation.features.auth.login

data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String = "",
    val token: String? = null
)