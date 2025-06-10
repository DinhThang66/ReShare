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
    val formatted_address: String,
    val address_components: List<AddressComponent>,
    val geometry: Geometry
)

data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)

data class Geometry(
    val location: LatLngResult
)

data class LatLngResult(
    val lat: Double,
    val lng: Double
)