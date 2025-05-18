package com.example.reshare.presentation.features.mainGraph.community

import com.example.reshare.domain.model.Post

sealed class CommunityUiEvent {
    data object Refresh : CommunityUiEvent()
    data object LoadNextPage : CommunityUiEvent()

    data class ToggleLike(val postId: String) : CommunityUiEvent()
    data class UpdatePost(val post: Post) : CommunityUiEvent()
}