package com.example.reshare.data.remote.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class UserDto(
    val _id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val profilePic: String,
    val location: Location?
)

data class LocationDto(
    val type: String, // "Point"
    val coordinates: List<Double> // [lng, lat]
)

data class LoginDto(
    val token: String,
    val user: UserDto,
    val hasLocation: Boolean
)

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

data class RegisterDto(
    val token: String,
    val user: UserDto
)

data class UpdateLocationDto(
    val user: UserDto,
    val hasLocation: Boolean
)

data class LocationRequest(
    val latitude: Double,
    val longitude: Double
)