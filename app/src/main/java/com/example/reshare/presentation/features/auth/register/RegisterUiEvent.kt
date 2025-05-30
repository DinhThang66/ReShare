package com.example.reshare.presentation.features.auth.register

sealed class RegisterUiEvent {
    data class Submit(
        val firstName: String,
        val lastName: String,
        val email: String,
        val password: String
    ) : RegisterUiEvent()

    data class OnFirstNameChange(val firstName: String) : RegisterUiEvent()
    data class OnLastNameChange(val lastName: String) : RegisterUiEvent()
    data class OnEmailNameChange(val email: String) : RegisterUiEvent()
    data class OnPasswordChange(val password: String) : RegisterUiEvent()
    data class OnConfirmPasswordChange(val confirmPassword: String) : RegisterUiEvent()

    data class ValidateForm(
        val firstName: String,
        val lastName: String,
        val email: String,
        val password: String,
        val confirmPassword: String
    ): RegisterUiEvent()
}