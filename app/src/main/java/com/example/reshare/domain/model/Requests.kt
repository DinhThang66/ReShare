package com.example.reshare.domain.model

data class Requests(
    val id: String,
    val productId: Product,
    val pickupTime: String,
    val status: String,
    val createdAt: String
)

data class Requests2(
    val id: String,
    val productId: Product,
    val pickupTime: String,
    val requestedBy: User,
    val status: String,
    val createdAt: String
)