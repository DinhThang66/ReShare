package com.example.reshare.data.remote.dto

data class RequestResponseDto(
    val message: String,
    val request: RequestDto
)

data class RequestDto (
    val id: String,
    val productId: String,
    val requestedBy: String,
    val pickupTime: String,
    val status: String,
    val createdAt: String
)

data class SendRequestBody(
    val productId: String,
    val pickupTime: String,
    val message: String? = null
)