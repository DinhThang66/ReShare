package com.example.reshare.presentation.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.data.local.UserPreferences
import com.example.reshare.domain.usecase.auth.LoginUseCase
import com.example.reshare.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.Submit -> {
                val errorMessage = validateInput(event.email, event.password)
                if (errorMessage != null) {
                    _state.update { it.copy(error = errorMessage) }
                    return
                }
                login(event.email, event.password)
            }
            is LoginUiEvent.OnEmailChange -> {
                _state.update { it.copy(email = event.email) }
            }
            is LoginUiEvent.OnPasswordChange -> {
                _state.update { it.copy(password = event.password) }
            }
        }
    }

    private fun login(email: String, password: String) {
        _state.update { it.copy(isLoading = true, error = "") }
        viewModelScope.launch {
            when (val result = loginUseCase(email, password)) {
                is Resource.Success -> {
                    val user = result.data!!.user
                    val token = result.data.token
                    _state.update {
                        it.copy(isLoading = false, token = token)
                    }
                    userPreferences.saveUser(
                        id = user.id,
                        firstName = user.firstName,
                        lastName = user.lastName,
                        email = user.email,
                        profilePic = user.profilePic,
                        latitude = user.latitude,
                        longitude = user.longitude,
                        hasLocation = result.data.hasLocation,
                        token = token
                    )
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validateInput(email: String, password: String): String? {
        return when {
            email.isBlank() || password.isBlank() -> "Email and password must not be empty."
            !isValidEmail(email) -> "Invalid email format."
            else -> null
        }
    }
}
