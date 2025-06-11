package com.example.reshare.presentation.features.mainGraph.home.makeARequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.domain.usecase.requests.SendProductRequestUseCase
import com.example.reshare.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakeRequestViewModel @Inject constructor(
    private val sendProductRequestUseCase: SendProductRequestUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MakeRequestState())
    val state: StateFlow<MakeRequestState> = _state.asStateFlow()

    fun onEvent(event: MakeRequestUiEvent) {
        when (event) {
            is MakeRequestUiEvent.PickupTimeChanged -> {
                _state.update { it.copy(pickupTime = event.value, error = "") }
            }
            is MakeRequestUiEvent.MessageChanged -> {
                _state.update { it.copy(message = event.value) }
            }
            is MakeRequestUiEvent.SubmitRequest -> {
                submitRequest(event.productId)
            }
            is MakeRequestUiEvent.SetProduct -> {
                _state.update { it.copy(product = event.product) }
            }
        }
    }

    private fun submitRequest(productId: String) {
        val current = _state.value
        if (current.pickupTime.isBlank()) {
            _state.update { it.copy(error = "Pickup time cannot be blank") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = "", success = false) }
            when (val result = sendProductRequestUseCase(
                productId = productId,
                pickupTime = current.pickupTime,
                message = current.message.ifBlank { null }
            )) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(isLoading = false, success = true)
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(isLoading = false, error = result.message ?: "Unknown error")
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }
}