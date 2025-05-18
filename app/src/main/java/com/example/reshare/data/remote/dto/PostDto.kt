package com.example.reshare.data.remote.dto

data class PostListDto(
    val posts: List<PostDto>,
    val currentPage: Int,
    val totalPages: Int
)

data class PostDto(
    val _id: String,
    val content: String,
    val images: List<String>,
    val createdBy: UserDto,
    val commentsCount: Int,
    val likesCount: Int,
    val likedByCurrentUser: Boolean,
    val createdAt: String,
    val updatedAt: String,
)

data class LikeResponseDto(
    val likes: Int,
    val liked: Boolean
)