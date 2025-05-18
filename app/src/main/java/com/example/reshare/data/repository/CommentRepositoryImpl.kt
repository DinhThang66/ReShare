package com.example.reshare.data.repository

import com.example.reshare.data.mapper.toDomain
import com.example.reshare.data.remote.AppApi
import com.example.reshare.data.remote.dto.AddCommentRequest
import com.example.reshare.domain.model.Comment
import com.example.reshare.domain.repository.CommentRepository
import com.example.reshare.presentation.utils.Resource

class CommentRepositoryImpl(
    private val api: AppApi
) : CommentRepository {
    override suspend fun getCommentsByPost(postId: String): Resource<List<Comment>> {
        return try {
            val response = api.getCommentsByPost(postId)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val messages = body.comments.map { it.toDomain() }
                Resource.Success(messages)
            } else {
                Resource.Error("Error server: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Unexpected error: ${e.message ?: "Unknown error"}")
        }
    }

    override suspend fun addComment(content: String, postId: String): Resource<Comment> {
        return try {
            val response = api.addComment(AddCommentRequest(
                content = content, postId = postId
            ))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                Resource.Success(body.toDomain())
            } else {
                Resource.Error("Error server: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Unexpected error: ${e.message ?: "Unknown error"}")
        }
    }
}