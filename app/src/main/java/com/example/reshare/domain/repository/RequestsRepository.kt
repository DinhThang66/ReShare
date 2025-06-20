package com.example.reshare.domain.repository

import com.example.reshare.domain.model.Requests
import com.example.reshare.domain.model.Requests2
import com.example.reshare.presentation.utils.Resource

interface RequestsRepository {
    suspend fun sendProductRequest(
        productId: String,
        pickupTime: String,
        message: String? = null
    ): Resource<String>

    suspend fun getMyRequests(): Resource<List<Requests>>
    suspend fun getReceivedRequests(): Resource<List<Requests2>>
    suspend fun updateRequestStatus(
        requestId: String,
        status: String
    ): Resource<String>
}