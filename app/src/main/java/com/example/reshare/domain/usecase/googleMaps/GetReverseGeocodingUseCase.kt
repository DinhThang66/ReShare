package com.example.reshare.domain.usecase.googleMaps

import com.example.reshare.domain.repository.PlacesRepository
import com.example.reshare.presentation.utils.Resource
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class GetReverseGeocodingUseCase @Inject constructor(
    private val repository: PlacesRepository
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double
    ): Resource<String> {
        return repository.getReverseGeocoding(latitude, longitude)
    }
}