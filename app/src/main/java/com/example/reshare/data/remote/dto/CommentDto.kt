package com.example.reshare.data.remote.dto

data class CommentsDto(
    val comments: List<CommentDto>
)

data class CommentDto(
    val _id: String,
    val postId: String,
    val content: String,
    val createdBy: UserDto,
    val likes: List<String>,
    val createdAt: String,
    val updatedAt: String
)

data class AddCommentRequest(
    val content: String,
    val postId: String
)