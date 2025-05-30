package com.example.reshare.data.repository

import com.example.reshare.data.mapper.toDomain
import com.example.reshare.data.remote.AppApi
import com.example.reshare.data.remote.dto.LoginRequest
import com.example.reshare.data.remote.dto.RegisterRequest
import com.example.reshare.domain.model.AuthResult
import com.example.reshare.domain.repository.AuthRepository
import com.example.reshare.presentation.utils.Resource

class AuthRepositoryImpl(
    private val api: AppApi
) : AuthRepository {
    override suspend fun login(email: String, password: String): Resource<AuthResult> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                Resource.Success(
                    AuthResult(
                        token = body.token,
                        user = body.user.toDomain(),
                        hasLocation = body.hasLocation
                    )
                )
            } else {
                Resource.Error("Login failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error: $e")
        }
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Resource<AuthResult> {
        return try {
            val response = api.register(RegisterRequest(firstName, lastName, email, password))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                Resource.Success(
                    AuthResult(
                        token = body.token,
                        user = body.user.toDomain()
                    )
                )
            } else {
                Resource.Error("Register failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error: $e")
        }
    }
}
