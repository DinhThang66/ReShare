package com.example.reshare.domain.usecase.post

import com.example.reshare.domain.model.Post
import com.example.reshare.domain.repository.PostRepository
import javax.inject.Inject

class GetAllPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke() : Result<List<Post>> {
        return repository.getAllPosts()
    }
}