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
    val profilePic: String
)

data class LoginResponse(
    val token: String,
    val user: UserDto
)

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val token: String,
    val user: UserDto
)
