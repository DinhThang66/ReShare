package com.example.reshare.domain.repository

import com.example.reshare.domain.model.PlaceSuggestion
import com.example.reshare.presentation.utils.Resource
import com.google.android.gms.maps.model.LatLng

interface PlacesRepository {
    suspend fun searchPlaces(query: String): Resource<List<PlaceSuggestion>>
    suspend fun getCoordinates(placeId: String): Resource<LatLng>
    suspend fun getReverseGeocoding(
        latitude: Double,
        longitude: Double
    ): Resource<String>
}