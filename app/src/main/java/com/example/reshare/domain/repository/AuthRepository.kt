package com.example.reshare.domain.repository

import com.example.reshare.domain.model.AuthResult

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthResult>

    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<AuthResult>
}