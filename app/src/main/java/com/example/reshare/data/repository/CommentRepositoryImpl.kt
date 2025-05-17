package com.example.reshare.data.repository

import com.example.reshare.data.mapper.toDomain
import com.example.reshare.data.remote.AppApi
import com.example.reshare.data.remote.dto.AddCommentRequest
import com.example.reshare.domain.model.Comment
import com.example.reshare.domain.repository.CommentRepository

class CommentRepositoryImpl(
    private val api: AppApi
) : CommentRepository {
    override suspend fun getCommentsByPost(postId: String): Result<List<Comment>> {
        return try {
            val response = api.getCommentsByPost(postId)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val messages = body.comments.map { it.toDomain() }
                Result.success(messages)
            } else {
                Result.failure(Exception("Error server: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addComment(content: String, postId: String): Result<Comment> {
        return try {
            val response = api.addComment(AddCommentRequest(
                content = content, postId = postId
            ))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                Result.success(body.toDomain())
            } else {
                Result.failure(Exception("Error server: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}