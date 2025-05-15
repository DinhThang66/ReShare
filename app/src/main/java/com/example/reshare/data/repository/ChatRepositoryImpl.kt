package com.example.reshare.data.repository

import com.example.reshare.data.remote.AppApi
import com.example.reshare.domain.repository.ChatRepository

class ChatRepositoryImpl(
    private val api: AppApi
) : ChatRepository {
    override suspend fun getStreamToken(): Result<String> {
        return try {
            val response = api.getStreamToken()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.token)
            } else {
                Result.failure(Exception("Failed to get Stream token: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}