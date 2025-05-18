package com.example.reshare.domain.usecase.comment

import com.example.reshare.domain.model.Comment
import com.example.reshare.domain.repository.CommentRepository
import com.example.reshare.presentation.utils.Resource
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(content: String, postId: String) : Resource<Comment> {
        return repository.addComment(content, postId)
    }
}