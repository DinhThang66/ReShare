package com.example.reshare.data.repository

import com.example.reshare.data.mapper.toDomain
import com.example.reshare.data.remote.AppApi
import com.example.reshare.data.remote.dto.SendRequestBody
import com.example.reshare.data.remote.dto.UpdateStatusBody
import com.example.reshare.domain.model.Requests
import com.example.reshare.domain.model.Requests2
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

    override suspend fun getMyRequests(): Resource<List<Requests>> {
        return try {
            val response = api.getMyRequests()
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val messages = body.map { it.toDomain() }
                Resource.Success(messages)
            } else {
                Resource.Error("Error server: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Unexpected error: ${e.message ?: "Unknown error"}")
        }
    }

    override suspend fun getReceivedRequests(): Resource<List<Requests2>> {
        return try {
            val response = api.getReceivedRequests()
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val messages = body.map { it.toDomain() }
                Resource.Success(messages)
            } else {
                Resource.Error("Error server: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Unexpected error: ${e.message ?: "Unknown error"}")
        }
    }

    override suspend fun updateRequestStatus(requestId: String, status: String): Resource<String> {
        return try {
            val response = api.updateRequestStatus(
                requestId = requestId,
                body = UpdateStatusBody(status)
            )
            if (response.isSuccessful) {
                val message = response.body()?.message ?: "Status updated successfully"
                Resource.Success(message)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                Resource.Error(errorMsg)
            }
        } catch (e: Exception) {
            Resource.Error("Unexpected error: ${e.message ?: "Unknown error"}")
        }
    }
}