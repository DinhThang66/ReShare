package com.example.reshare.presentation.features.auth.login

sealed class LoginUiEvent {
    data class Submit(val email: String, val password: String) : LoginUiEvent()
    data class OnEmailChange(val email: String) : LoginUiEvent()
    data class OnPasswordChange(val password: String) : LoginUiEvent()
}
