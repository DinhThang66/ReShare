package com.example.reshare.data.remote

import com.example.reshare.data.remote.dto.AutoCompleteResponse
import com.example.reshare.data.remote.dto.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsApi {
    @GET("place/autocomplete/json")
    suspend fun getAutoCompletePlaces(
        @Query("input") input: String,
        @Query("key") apiKey: String
    ): AutoCompleteResponse

    @GET("geocode/json")
    suspend fun getGeocoding(
        @Query("place_id") placeId: String,
        @Query("key") apiKey: String
    ): GeocodingResponse
}