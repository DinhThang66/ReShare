package com.example.reshare.presentation.features.mainGraph.community.postDetail

import com.example.reshare.domain.model.Comment
import com.example.reshare.domain.model.Post

data class PostDetailState(
    val comments: List<Comment> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAddingComment: Boolean = false,
    val addCommentError: String? = null,
    val highlightedCommentId: String? = null,
    val post: Post? = null
)