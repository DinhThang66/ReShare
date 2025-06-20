package com.example.reshare.presentation.features.sideBar.myImpact

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.domain.usecase.requests.GetMyRequestsUseCase
import com.example.reshare.domain.usecase.requests.GetReceivedRequestsUseCase
import com.example.reshare.domain.usecase.requests.UpdateRequestStatusUseCase
import com.example.reshare.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyImpactViewModel @Inject constructor(
    private val getMyRequestsUseCase: GetMyRequestsUseCase,
    private val getReceivedRequestsUseCase: GetReceivedRequestsUseCase,
    private val updateRequestStatusUseCase: UpdateRequestStatusUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MyImpactState())
    val state = _state.asStateFlow()

    init {
        loadMyRequests()
    }

    fun onEvent(event: MyImpactUiEvent) {
        when (event) {
            is MyImpactUiEvent.RefreshMyRequests -> loadMyRequests()
            is MyImpactUiEvent.RefreshReceivedRequests -> loadReceivedRequests()
            is MyImpactUiEvent.UpdateRequestStatus -> updateStatus(
                event.requestId, event.status
            )
        }
    }

    private fun loadMyRequests() {
        viewModelScope.launch {
            _state.update { it.copy(isMyRequestsLoading = true, errorMyRequests = "") }

            when (val result = getMyRequestsUseCase()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            myRequests = result.data ?: emptyList(),
                            isMyRequestsLoading = false,
                            errorMyRequests = ""
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isMyRequestsLoading = false,
                            errorMyRequests = result.message ?: "Unknown error"
                        )
                    }
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    private fun loadReceivedRequests() {
        viewModelScope.launch {
            _state.update { it.copy(isReceivedRequestsLoading = true,
                initialRefresh = true,
                errorReceivedRequests = "") }

            when (val result = getReceivedRequestsUseCase()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            receivedRequests = result.data ?: emptyList(),
                            isReceivedRequestsLoading = false,
                            errorReceivedRequests = ""
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isReceivedRequestsLoading = false,
                            errorReceivedRequests = result.message ?: "Unknown error"
                        )
                    }
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    private fun updateStatus(requestId: String, status: String) {
        viewModelScope.launch {
            _state.update { it.copy(isUpdateStatus = true) }

            when (val result = updateRequestStatusUseCase(requestId, status)) {
                is Resource.Success -> {
                    Log.d("oke123", "oke")
                    _state.update { currentState ->
                        val updatedList = currentState.receivedRequests.map {
                            if (it.id == requestId) it.copy(status = status) else it
                        }
                        currentState.copy(receivedRequests = updatedList, isUpdateStatus = false)
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(isUpdateStatus = true)
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }
}