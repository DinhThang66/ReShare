package com.example.reshare.domain.repository

import com.example.reshare.domain.model.Comment

interface CommentRepository {
    suspend fun getCommentsByPost(postId: String): Result<List<Comment>>
    suspend fun addComment(content: String, postId: String): Result<Comment>
}