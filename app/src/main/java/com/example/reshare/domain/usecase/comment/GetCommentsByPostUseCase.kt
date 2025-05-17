package com.example.reshare.domain.usecase.comment

import com.example.reshare.domain.model.Comment
import com.example.reshare.domain.repository.CommentRepository
import javax.inject.Inject

class GetCommentsByPostUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(postId: String) : Result<List<Comment>> {
        return repository.getCommentsByPost(postId)
    }
}