package com.example.reshare.data.repository

import com.example.reshare.data.mapper.toDomain
import com.example.reshare.data.remote.AppApi
import com.example.reshare.data.remote.dto.SendRequestBody
import com.example.reshare.domain.repository.RequestsRepository
import com.example.reshare.presentation.utils.Resource

class RequestsRepositoryImpl(
    private val api: AppApi
) : RequestsRepository {
    override suspend fun sendProductRequest(
        productId: String,
        pickupTime: String,
        message: String?,
    ): Resource<String> {
        return try {
            val response = api.sendProductRequest(
                SendRequestBody(productId, pickupTime, message)
            )
            if (response.isSuccessful && response.body() != null) {
                // val body = response.body()!!
                Resource.Success("")
            } else {
                Resource.Error("Error server: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Unexpected error: ${e.message ?: "Unknown error"}")
        }
    }
}