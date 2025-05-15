package com.example.reshare.data.repository

import com.example.reshare.data.mapper.toDomain
import com.example.reshare.data.remote.AppApi
import com.example.reshare.data.remote.dto.LoginRequest
import com.example.reshare.data.remote.dto.RegisterRequest
import com.example.reshare.domain.model.AuthResult
import com.example.reshare.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AppApi
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<AuthResult> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                Result.success(
                    AuthResult(
                        token = body.token,
                        user = body.user.toDomain()
                    )
                )
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<AuthResult> {
        return try {
            val response = api.register(RegisterRequest(firstName, lastName, email, password))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                Result.success(
                    AuthResult(
                        token = body.token,
                        user = body.user.toDomain()
                    )
                )
            } else {
                Result.failure(Exception("Register failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
