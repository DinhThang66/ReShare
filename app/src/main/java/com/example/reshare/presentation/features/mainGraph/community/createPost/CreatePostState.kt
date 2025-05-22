package com.example.reshare.presentation.features.mainGraph.community.createPost

import android.net.Uri
import com.example.reshare.domain.model.User

data class CreatePostState (
    val user: User? = null,
    val selectedImages: List<Uri> = emptyList(),
    val postContent: String = "",
    val selectedCategory: String? = null,
    val isSubmitting: Boolean = false,
    val postSuccess: Boolean = false,
    val error: String? = null
)