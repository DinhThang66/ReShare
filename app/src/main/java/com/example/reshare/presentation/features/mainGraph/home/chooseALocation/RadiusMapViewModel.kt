package com.example.reshare.presentation.features.mainGraph.home.chooseALocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.domain.usecase.googleMaps.GetPlaceCoordinatesUseCase
import com.example.reshare.domain.usecase.googleMaps.SearchPlacesUseCase
import com.example.reshare.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RadiusMapViewModel @Inject constructor(
    private val searchPlacesUseCase: SearchPlacesUseCase,
    private val getPlaceCoordinatesUseCase: GetPlaceCoordinatesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(RadiusMapState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<RadiusMapSideEffect>()
    val sideEffect: SharedFlow<RadiusMapSideEffect> = _sideEffect

    fun onEvent(event: RadiusMapUiEvent) {
        when (event) {
            is RadiusMapUiEvent.OnQueryChange -> searchPlaces(event.query)
            is RadiusMapUiEvent.OnSuggestionSelected -> selectPlace(event.placeId)
            is RadiusMapUiEvent.OnRadiusChange -> updateRadius(event.miles)
            is RadiusMapUiEvent.OnSuggestionSelectedWithLatLng -> {
                _state.update {
                    it.copy(
                        selectedLocation = event.latLng,
                        suggestions = emptyList(),
                        isLoading = false
                    )
                }
                viewModelScope.launch {
                    _sideEffect.emit(RadiusMapSideEffect.LocationUpdated)
                }
            }
            RadiusMapUiEvent.OnApply -> applyRadius()
            RadiusMapUiEvent.OnClose -> closeScreen()
        }
    }

    private fun searchPlaces(query: String) {
        _state.update { it.copy(searchQuery = query, isLoading = true) }
        viewModelScope.launch {
            when (val result = searchPlacesUseCase(query)) {
                is Resource.Success -> _state.update {
                    it.copy(suggestions = result.data ?: emptyList(), isLoading = false)
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _sideEffect.emit(RadiusMapSideEffect.ShowError(result.message ?: "Search error"))
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun selectPlace(placeId: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = getPlaceCoordinatesUseCase(placeId)) {
                is Resource.Success -> {
                    result.data?.let { latLng ->
                        _state.update {
                            it.copy(
                                selectedLocation = latLng,
                                suggestions = emptyList(),
                                isLoading = false
                            )
                        }
                        _sideEffect.emit(RadiusMapSideEffect.LocationUpdated)
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _sideEffect.emit(RadiusMapSideEffect.ShowError(result.message ?: "Coordinate error"))
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun updateRadius(miles: Float) {
        _state.update { it.copy(radiusMiles = miles) }
    }

    private fun applyRadius() {
        // Logic Apply radius nếu cần gửi về hoặc xử lý thêm
    }

    private fun closeScreen() {
        viewModelScope.launch {
            _sideEffect.emit(RadiusMapSideEffect.CloseScreen)
        }
    }
}