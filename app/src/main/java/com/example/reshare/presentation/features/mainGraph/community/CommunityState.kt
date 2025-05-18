package com.example.reshare.presentation.features.mainGraph.community

import com.example.reshare.domain.model.Post

data class CommunityState(
    val isInitialLoading: Boolean = false,
    val isPaginating: Boolean = false,
    val error: String? = null,
    val posts: List<Post> = emptyList(),
    val currentPage: Int = 1,
    val isLastPage: Boolean = false
)