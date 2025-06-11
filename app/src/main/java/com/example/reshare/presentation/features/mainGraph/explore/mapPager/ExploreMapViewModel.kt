package com.example.reshare.presentation.features.mainGraph.explore.mapPager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.data.local.UserPreferences
import com.example.reshare.domain.usecase.product.GetNearbyProductsUseCase
import com.example.reshare.presentation.features.mainGraph.home.chooseALocation.ChooseALocationState
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreMapViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _state = MutableStateFlow(ExploreMapState())
    val state = _state.asStateFlow()

    init {
        setInit()
    }

    private fun setInit() {
        viewModelScope.launch {
            userPreferences.getUserFlow().collect { user ->
                val lat = user?.latitude
                val lng = user?.longitude
                val radius = user?.radius
                if (lat != null && lng != null && radius != null) {
                    _state.update { it.copy(
                        selectedLocation = LatLng(lat, lng),
                        radius = radius
                    ) }
                }
            }
        }
    }
}