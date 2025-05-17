package com.example.reshare.domain.usecase.comment

import com.example.reshare.domain.model.Comment
import com.example.reshare.domain.repository.CommentRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(content: String, postId: String) : Result<Comment> {
        return repository.addComment(content, postId)
    }
}