package com.example.reshare.presentation.features.auth.login

sealed class LoginEvent {
    data class Submit(val email: String, val password: String) : LoginEvent()
}
