package com.example.reshare.domain.repository

import com.example.reshare.presentation.utils.Resource

interface RequestsRepository {
    suspend fun sendProductRequest(
        productId: String,
        pickupTime: String,
        message: String? = null
    ): Resource<String>
}