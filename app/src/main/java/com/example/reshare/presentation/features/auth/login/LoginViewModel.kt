package com.example.reshare.presentation.features.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.data.local.UserPreferences
import com.example.reshare.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.Submit -> {
                val errorMessage = validateInput(event.email, event.password)
                if (errorMessage != null) {
                    state = state.copy(error = errorMessage)
                    return
                }

                login(event.email, event.password)
            }
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = "")
            val result = loginUseCase(email, password)

            state = if (result.isSuccess) {
                val authResult = result.getOrNull()
                if (authResult != null) {
                    userPreferences.saveUser(
                        id = authResult.user.id,
                        firstName = authResult.user.firstName,
                        lastName = authResult.user.lastName,
                        email = authResult.user.email,
                        profilePic = authResult.user.profilePic,
                        token = authResult.token
                    )
                }

                state.copy(
                    isLoading = false,
                    isSuccess = true,
                    token = authResult?.token
                )
            } else {
                state.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Unknown error"
                )
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
