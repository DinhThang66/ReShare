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

data class MyRequestsDto (
    val _id: String,
    val productId: ProductDto,
    val pickupTime: String,
    val status: String,
    val createdAt: String
)


data class ReceivedRequestsDto (
    val _id: String,
    val productId: ProductDto,
    val pickupTime: String,
    val requestedBy: UserDto,
    val status: String,
    val createdAt: String
)

data class UpdateStatusBody(
    val status: String
)

data class UpdateStatusResponse(
    val message: String
)