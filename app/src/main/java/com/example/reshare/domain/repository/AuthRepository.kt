package com.example.reshare.domain.repository

import com.example.reshare.domain.model.AuthResult
import com.example.reshare.presentation.utils.Resource

interface AuthRepository {
    suspend fun login(email: String, password: String): Resource<AuthResult>

    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Resource<AuthResult>

    suspend fun updateLocation(latitude: Double, longitude: Double): Resource<String>
}