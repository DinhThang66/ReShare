package com.example.reshare.data.repository

import com.example.reshare.data.mapper.toDomain
import com.example.reshare.data.remote.AppApi
import com.example.reshare.domain.model.User
import com.example.reshare.domain.repository.UserRepository
import com.example.reshare.presentation.utils.Resource

class UserRepositoryImpl(
    private val api: AppApi,
) : UserRepository {
    override suspend fun getUser(userId: String): Resource<User> {
        return try {
            val response = api.getUser(userId)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val user = body.toDomain()
                Resource.Success(user)
            } else {
                Resource.Error("Error server: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Unexpected error: ${e.message ?: "Unknown error"}")
        }
    }
}