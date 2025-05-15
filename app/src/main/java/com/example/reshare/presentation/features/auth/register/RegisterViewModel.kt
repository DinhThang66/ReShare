package com.example.reshare.presentation.features.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.domain.usecase.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.Submit -> {
                register(event.firstName, event.lastName, event.email, event.password)
            }
        }
    }

    private fun register(firstName: String, lastName: String, email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = "")

            val result = registerUseCase(firstName, lastName, email, password)

            state = if (result.isSuccess) {
                RegisterState(isSuccess = true)
            } else {
                RegisterState(error = result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}