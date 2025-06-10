package com.example.reshare.data.repository

import android.util.Log
import com.example.reshare.data.remote.GoogleMapsApi
import com.example.reshare.domain.model.PlaceSuggestion
import com.example.reshare.domain.repository.PlacesRepository
import com.example.reshare.presentation.utils.Resource
import com.example.reshare.presentation.utils.getStreetOrDistrict
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val api: GoogleMapsApi,
    private val apiKey: String
) : PlacesRepository {

    override suspend fun searchPlaces(query: String): Resource<List<PlaceSuggestion>> {
        return try {
            val response = api.getAutoCompletePlaces(query, apiKey)
            val suggestions = response.predictions.map {
                PlaceSuggestion(
                    description = it.description,
                    placeId = it.place_id
                )
            }
            Resource.Success(suggestions)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred while searching")
        }
    }

    override suspend fun getCoordinates(placeId: String): Resource<LatLng> {
        return try {
            val response = api.getGeocoding(placeId, apiKey)
            val location = response.results.firstOrNull()?.geometry?.location
            if (location != null) {
                Resource.Success(LatLng(location.lat, location.lng))
            } else {
                Resource.Error("Coordinates not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred while getting coordinates")
        }
    }

    override suspend fun getReverseGeocoding(
        latitude: Double,
        longitude: Double,
    ): Resource<String> {
        return try {
            val latLng = "$latitude,$longitude"
            val response = api.getReverseGeocoding(latLng, apiKey)

            val result = response.results.firstOrNull()
            val name = result?.let { getStreetOrDistrict(it) }

            Log.d("result", result.toString())
            if (name != null) {
                Resource.Success(name)
            } else {
                Resource.Error("Error not found!!")
            }
        } catch (e: Exception) {
            Log.d("result", "oke")
            Resource.Error(e.localizedMessage ?: "Error reverse geocoding")
        }
    }
}