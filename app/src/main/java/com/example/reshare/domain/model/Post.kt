package com.example.reshare.domain.model

data class Post(
    val id: String,
    val content: String,
    val createdBy: User,
    val images: List<String>,
    val likes: List<Any>,
    val createdAt: String,
    val updatedAt: String,
)