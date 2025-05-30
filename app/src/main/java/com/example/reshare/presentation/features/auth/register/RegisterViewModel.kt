package com.example.reshare.presentation.features.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.domain.usecase.auth.RegisterUseCase
import com.example.reshare.presentation.features.auth.login.LoginState
import com.example.reshare.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.Submit -> {
                register(event.firstName, event.lastName, event.email, event.password)
            }
            is RegisterUiEvent.OnFirstNameChange -> {
                _state.update { it.copy(firstName = event.firstName) }
            }
            is RegisterUiEvent.OnLastNameChange -> {
                _state.update { it.copy(lastName = event.lastName) }
            }
            is RegisterUiEvent.OnEmailNameChange -> {
                _state.update { it.copy(email = event.email) }
            }
            is RegisterUiEvent.OnPasswordChange -> {
                _state.update { it.copy(password = event.password) }
            }
            is RegisterUiEvent.OnConfirmPasswordChange -> {
                _state.update { it.copy(confirmPassword = event.confirmPassword) }
            }
            is RegisterUiEvent.ValidateForm -> { validateForm(
                firstName = event.firstName,
                lastName = event.lastName,
                email = event.email,
                password = event.password,
                confirmPassword = event.confirmPassword
            ) }
        }
    }

    private fun register(firstName: String, lastName: String, email: String, password: String) {
        _state.update { it.copy(isLoading = true, error = "") }

        viewModelScope.launch {
            when (
                val result = registerUseCase(firstName, lastName, email, password)
            ) {
                is Resource.Success -> { _state.update {it.copy(isSuccess = true)} }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Unknown error"
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun validateForm(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        val firstNameError = if (firstName.isBlank()) "First name can not be blank" else ""
        val lastNameError = if (lastName.isBlank()) "Last name can not be blank" else ""
        val emailError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            "Oh no, your email doesn't seem right. Please try again" else ""
        val passwordError = if (password.length < 8)
            "Passwords should be at least 8 characters" else ""
        val confirmPasswordError = when {
            confirmPassword.isBlank() -> "Confirm password cannot be blank"
            confirmPassword != password -> "Passwords do not match"
            else -> ""
        }

        val isValid = listOf(
            firstNameError,
            lastNameError,
            emailError,
            passwordError,
            confirmPasswordError
        ).all { it.isBlank() }

        _state.update {
            it.copy(
                firstNameError = firstNameError,
                lastNameError = lastNameError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError,
                isValid = isValid
            )
        }
    }
}