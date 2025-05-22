package com.example.reshare.domain.usecase.post

import com.example.reshare.domain.model.Post
import com.example.reshare.domain.repository.PostRepository
import com.example.reshare.presentation.utils.Resource
import okhttp3.MultipartBody
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        content: String,
        images: List<MultipartBody.Part>
    ): Resource<Post> {
        return repository.createPost(content, images)
    }
}