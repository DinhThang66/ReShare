package com.example.reshare.data.remote.dto

data class AutoCompleteResponse(
    val predictions: List<Prediction>,
    val status: String
)

data class Prediction(
    val description: String,
    val place_id: String
)

data class GeocodingResponse(
    val results: List<GeocodingResult>,
    val status: String
)

data class GeocodingResult(
    val geometry: Geometry
)

data class Geometry(
    val location: LatLngResult
)

data class LatLngResult(
    val lat: Double,
    val lng: Double
)