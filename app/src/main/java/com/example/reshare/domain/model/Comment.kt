package com.example.reshare.domain.model

data class Comment(
    val id: String,
    val postId: String,
    val content: String,
    val createdBy: User,
    val likes: List<String>,
    val createdAt: String,
    val updatedAt: String
)