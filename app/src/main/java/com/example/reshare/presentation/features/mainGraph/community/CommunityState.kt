package com.example.reshare.presentation.features.mainGraph.community

import com.example.reshare.domain.model.Post

data class CommunityState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val posts: List<Post> = emptyList(),
    val page: Int = 1       // Dự phòng nếu muốn phân trang
)