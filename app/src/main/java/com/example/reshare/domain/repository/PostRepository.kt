package com.example.reshare.domain.repository

import com.example.reshare.data.remote.dto.LikeResponseDto
import com.example.reshare.domain.model.Like
import com.example.reshare.domain.model.PagedResult
import com.example.reshare.domain.model.Post
import com.example.reshare.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPosts(
        forceFetchFromRemote: Boolean,
        page: Int
    ): Flow<Resource<PagedResult<Post>>>

    suspend fun toggleLike(postId: String): Resource<Like>
}