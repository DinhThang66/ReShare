package com.example.reshare.domain.repository

import com.example.reshare.data.remote.dto.LikeResponseDto
import com.example.reshare.domain.model.Like
import com.example.reshare.domain.model.PagedResult
import com.example.reshare.domain.model.Post
import com.example.reshare.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface PostRepository {
    suspend fun getPosts(
        forceFetchFromRemote: Boolean,
        page: Int
    ): Flow<Resource<PagedResult<Post>>>

    suspend fun toggleLike(postId: String): Resource<Like>
    suspend fun createPost(
        content: String,
        images: List<MultipartBody.Part>
    ): Resource<Post>
}