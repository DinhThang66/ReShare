package com.example.reshare.domain.usecase.googleMaps

import com.example.reshare.domain.repository.PlacesRepository
import com.example.reshare.presentation.utils.Resource
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class GetPlaceCoordinatesUseCase @Inject constructor(
    private val repository: PlacesRepository
) {
    suspend operator fun invoke(placeId: String): Resource<LatLng> {
        return repository.getCoordinates(placeId)
    }
}