package com.example.reshare.domain.repository

import com.example.reshare.domain.model.Post
import com.example.reshare.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getAllPosts(
        forceFetchFromRemote: Boolean
    ): Flow<Resource<List<Post>>>
}