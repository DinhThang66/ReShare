package com.example.reshare.presentation.features.auth.setYourLocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.data.local.UserPreferences
import com.example.reshare.domain.usecase.auth.UpdateLocationUseCase
import com.example.reshare.domain.usecase.googleMaps.GetPlaceCoordinatesUseCase
import com.example.reshare.domain.usecase.googleMaps.SearchPlacesUseCase
import com.example.reshare.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetYourLocationViewModel @Inject constructor(
    private val searchPlacesUseCase: SearchPlacesUseCase,
    private val getPlaceCoordinatesUseCase: GetPlaceCoordinatesUseCase,
    private val updateLocationUseCase: UpdateLocationUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _state = MutableStateFlow(SetYourLocationState())
    val state = _state.asStateFlow()

    fun onEvent(event: SetYourLocationUiEvent) {
        when(event) {
            is SetYourLocationUiEvent.OnQueryChange -> { searchPlaces(event.query) }
            is SetYourLocationUiEvent.OnSuggestionSelected -> { selectPlace(event.placeId) }
            is SetYourLocationUiEvent.OnCameraMoved -> {
                _state.update { it.copy(selectedLocation = event.latLng) }
            }
            is SetYourLocationUiEvent.OnSuggestionSelectedWithLatLng -> {
                _state.update {
                    it.copy(
                        selectedLocation = event.latLng,
                        suggestions = emptyList(),
                        isRequestingLocation = event.isRequesting
                    )
                }
            }
            is SetYourLocationUiEvent.OnZoomChanged -> {
                _state.update { it.copy(zoomLevel = event.zoom) }
            }
            is SetYourLocationUiEvent.SetLocation -> {
                updateLocation(event.latitude, event.longitude)
            }
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
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun updateLocation(latitude: Double, longitude: Double) {
        _state.update { it.copy(isLoading = true, error = "") }
        viewModelScope.launch {
            when (val result = updateLocationUseCase(latitude, longitude)) {
                is Resource.Success -> {
                    userPreferences.saveHasLocation(
                        hasLocation = true,
                        latitude = latitude, longitude = longitude
                    )
                    _state.update { it.copy(isLoading = false) }
                }
                is Resource.Error -> { _state.update {
                    it.copy(isLoading = false, error = result.message?:"") }
                }
                is Resource.Loading -> Unit
            }
        }
    }
}