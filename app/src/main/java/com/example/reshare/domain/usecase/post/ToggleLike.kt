package com.example.reshare.domain.usecase.post

import com.example.reshare.domain.model.Like
import com.example.reshare.domain.repository.PostRepository
import com.example.reshare.presentation.utils.Resource
import javax.inject.Inject

class ToggleLike @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: String): Resource<Like> {
        return repository.toggleLike(postId)
    }
}