package com.example.reshare.presentation.features.mainGraph.community.postDetail

import com.example.reshare.domain.model.Post

sealed class PostDetailUiEvent {
    data class LoadComments(val postId: String) : PostDetailUiEvent()
    data class AddComment(val content: String, val postId: String) : PostDetailUiEvent()
    data object ResetAddCommentState : PostDetailUiEvent()

    data class SetPost(val post: Post) : PostDetailUiEvent()
    data class ToggleLike(val postId: String) : PostDetailUiEvent()
}