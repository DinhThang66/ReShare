package com.example.reshare.domain.repository

import com.example.reshare.domain.model.Comment
import com.example.reshare.presentation.utils.Resource

interface CommentRepository {
    suspend fun getCommentsByPost(postId: String): Resource<List<Comment>>
    suspend fun addComment(content: String, postId: String): Resource<Comment>
}