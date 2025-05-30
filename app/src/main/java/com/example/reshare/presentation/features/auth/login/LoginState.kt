package com.example.reshare.presentation.features.auth.login

data class LoginState(
    val isLoading: Boolean = false,
    val error: String = "",

    val email: String = "",
    val password: String = "",
    val token: String? = null
)