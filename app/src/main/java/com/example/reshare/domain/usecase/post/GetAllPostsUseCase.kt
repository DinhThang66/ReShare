package com.example.reshare.domain.usecase.post

import com.example.reshare.domain.model.Post
import com.example.reshare.domain.repository.PostRepository
import com.example.reshare.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        forceFetchFromRemote: Boolean,
    ) : Flow<Resource<List<Post>>> {
        return repository.getAllPosts(forceFetchFromRemote)
    }
}