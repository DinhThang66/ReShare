package com.example.reshare.presentation.features.mainGraph.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.domain.usecase.product.GetCategorizedProductsUseCase
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
class HomeViewModel @Inject constructor(
    private val getCategorizedProductsUseCase : GetCategorizedProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadProducts()
    }
    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.Refresh -> loadProducts()
        }
    }

    private fun loadProducts(forceRefresh: Boolean = true) {
        viewModelScope.launch {
            getCategorizedProductsUseCase(forceRefresh).collectLatest { result ->
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
                        val categorized = result.data
                        _state.update {
                            it.copy(
                                freeFood = categorized?.freeFood ?: emptyList(),
                                nonFood = categorized?.nonFood ?: emptyList(),
                                reducedFood = categorized?.reducedFood ?: emptyList(),
                                want = categorized?.want ?: emptyList(),
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