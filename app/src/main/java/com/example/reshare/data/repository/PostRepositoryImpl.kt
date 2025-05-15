package com.example.reshare.data.repository

import com.example.reshare.data.mapper.toDomain
import com.example.reshare.data.remote.AppApi
import com.example.reshare.domain.model.Post
import com.example.reshare.domain.repository.PostRepository

class PostRepositoryImpl (
    private val api: AppApi
) : PostRepository {
    override suspend fun getAllPosts(): Result<List<Post>> {
        return try {
            val response = api.getAllPosts()
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val posts = body.posts.map { it.toDomain() }
                Result.success(posts)
            } else {
                Result.failure(Exception("Error server: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}