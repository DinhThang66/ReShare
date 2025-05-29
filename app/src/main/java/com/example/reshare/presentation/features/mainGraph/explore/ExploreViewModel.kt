package com.example.reshare.presentation.features.mainGraph.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.domain.usecase.product.GetNearbyProductsUseCase
import com.example.reshare.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel  @Inject constructor(
    private val getNearbyProductsUseCase: GetNearbyProductsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ExploreState())
    val state: StateFlow<ExploreState> = _state.asStateFlow()

    init {
        loadProducts()
    }
    fun onEvent(event: ExploreUiEvent) {
        when (event) {
            is ExploreUiEvent.Refresh -> loadProducts()
        }
    }
    private fun loadProducts(forceRefresh: Boolean = true) {
        viewModelScope.launch {
            getNearbyProductsUseCase(forceRefresh).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = result.isLoading) }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Unknown error"
                            )
                        }
                    }
                    is Resource.Success -> {
                        val products = result.data
                        _state.update {
                            it.copy(
                                products = products ?: emptyList(),
                                isLoading = false,
                                error = ""
                            )
                        }
                    }
                }
            }
        }
    }
}