package com.example.reshare.data.remote.dto

data class PostsDto(
    val posts: List<PostDto>
)

data class PostDto(
    val _id: String,
    val content: String,
    val createdBy: UserDto,
    val images: List<String>,
    val likes: List<Any>,
    val createdAt: String,
    val updatedAt: String,
)