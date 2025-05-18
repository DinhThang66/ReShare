package com.example.reshare.domain.usecase.comment

import com.example.reshare.domain.model.Comment
import com.example.reshare.domain.repository.CommentRepository
import com.example.reshare.presentation.utils.Resource
import javax.inject.Inject

class GetCommentsByPostUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(postId: String) : Resource<List<Comment>> {
        return repository.getCommentsByPost(postId)
    }
}