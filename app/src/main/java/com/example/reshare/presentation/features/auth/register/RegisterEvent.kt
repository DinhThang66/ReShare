package com.example.reshare.presentation.features.auth.register

sealed class RegisterEvent {
    data class Submit(
        val firstName: String,
        val lastName: String,
        val email: String,
        val password: String
    ) : RegisterEvent()
}