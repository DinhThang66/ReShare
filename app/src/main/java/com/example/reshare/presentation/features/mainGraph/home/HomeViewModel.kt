package com.example.reshare.presentation.features.mainGraph.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.data.local.UserPreferences
import com.example.reshare.domain.usecase.googleMaps.GetReverseGeocodingUseCase
import com.example.reshare.domain.usecase.product.GetCategorizedProductsUseCase
import com.example.reshare.presentation.utils.Resource
import com.google.android.gms.maps.model.LatLng
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
    private val getCategorizedProductsUseCase : GetCategorizedProductsUseCase,
    private val getReverseGeocodingUseCase: GetReverseGeocodingUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadProducts()
        loadUserLocation()
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

    private fun loadUserLocation() {
        viewModelScope.launch {
            userPreferences.getUserFlow().collect { user ->
                val lat = user?.latitude
                val lng = user?.longitude

                if (lat != null && lng != null) {
                    when (val result = getReverseGeocodingUseCase(lat, lng)) {
                        is Resource.Success -> {
                            val locationName = result.data ?: ""
                            _state.update { it.copy(userLocation = locationName) }
                            Log.d("result", locationName)
                        }
                        is Resource.Error -> {
                            _state.update { it.copy(error = result.message ?: "Error") }
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}