package com.example.reshare.presentation.features.mainGraph.home.chooseALocation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.data.local.UserPreferences
import com.example.reshare.domain.usecase.auth.UpdateLocationUseCase
import com.example.reshare.domain.usecase.googleMaps.GetPlaceCoordinatesUseCase
import com.example.reshare.domain.usecase.googleMaps.SearchPlacesUseCase
import com.example.reshare.presentation.utils.Resource
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseALocationViewModel @Inject constructor(
    private val searchPlacesUseCase: SearchPlacesUseCase,
    private val getPlaceCoordinatesUseCase: GetPlaceCoordinatesUseCase,
    private val updateLocationUseCase: UpdateLocationUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _state = MutableStateFlow(ChooseALocationState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ChooseALocationSideEffect>()
    val sideEffect: SharedFlow<ChooseALocationSideEffect> = _sideEffect

    init {
        setLngLat()
        setRadius()
    }

    fun onEvent(event: ChooseALocationUiEvent) {
        when (event) {
            is ChooseALocationUiEvent.OnQueryChange -> searchPlaces(event.query)
            is ChooseALocationUiEvent.OnSuggestionSelected -> selectPlace(event.placeId)
            is ChooseALocationUiEvent.OnRadiusChange -> updateRadius(event.km)
            is ChooseALocationUiEvent.OnSuggestionSelectedWithLatLng -> {
                _state.update {
                    it.copy(
                        selectedLocation = event.latLng,
                        suggestions = emptyList(),
                        isRequestingLocation = event.isRequesting
                    )
                }
                viewModelScope.launch {
                    _sideEffect.emit(ChooseALocationSideEffect.LocationUpdated)
                }
            }
            is ChooseALocationUiEvent.OnApply -> applyRadius(event.latLng, event.km)
            ChooseALocationUiEvent.OnClose -> closeScreen()
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
                    _sideEffect.emit(ChooseALocationSideEffect.ShowError(result.message ?: "Search error"))
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
                        _sideEffect.emit(ChooseALocationSideEffect.LocationUpdated)
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _sideEffect.emit(ChooseALocationSideEffect.ShowError(result.message ?: "Coordinate error"))
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun updateRadius(km: Float) {
        _state.update { it.copy(radiusKm = km) }
    }

    private fun setLngLat() {
        viewModelScope.launch {
            userPreferences.getUserFlow().collect { user ->
                val lat = user?.latitude
                val lng = user?.longitude
                if (lat != null && lng != null) {
                    _state.update { it.copy(selectedLocation = LatLng(lat, lng)) }
                }
            }
        }
    }

    private fun setRadius() {
        viewModelScope.launch {
            userPreferences.getUserFlow().collect { user ->
                val radius = user?.radius
                if (radius != null)
                    _state.update { it.copy(radiusKm = radius) }
            }
        }
    }

    private fun closeScreen() {
        viewModelScope.launch {
            _sideEffect.emit(ChooseALocationSideEffect.CloseScreen)
        }
    }

    private fun applyRadius(latLng: LatLng, km: Float) {
        _state.update { it.copy(isLoading = true, error = "") }
        viewModelScope.launch {

            when (val result =
                updateLocationUseCase(
                    latitude = latLng.latitude, longitude = latLng.longitude,
                    radius = km
                )
            ) {
                is Resource.Success -> {
                    userPreferences.saveHasLocation(
                        hasLocation = true,
                        latitude = latLng.latitude, longitude = latLng.longitude,
                        radius = km
                    )
                    _state.update { it.copy(isLoading = false) }
                    _sideEffect.emit(ChooseALocationSideEffect.LocationUpdatedAndClose)
                }
                is Resource.Error -> { _state.update {
                    it.copy(isLoading = false, error = result.message?:"") }
                }
                is Resource.Loading -> Unit
            }
        }
    }
}