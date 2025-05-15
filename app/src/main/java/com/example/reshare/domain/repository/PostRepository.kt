package com.example.reshare.domain.repository

import com.example.reshare.domain.model.Post

interface PostRepository {
    suspend fun getAllPosts(): Result<List<Post>>
}