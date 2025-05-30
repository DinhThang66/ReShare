package com.example.reshare.domain.model

data class AuthResult(
    val token: String,
    val user: User,
    val hasLocation: Boolean = false
)