package com.example.reshare.presentation.features.mainGraph.community.createPost

import android.net.Uri

sealed class CreatePostUiEvent {
    data object SubmitPost : CreatePostUiEvent()
    data class UpdateContent(val text: String) : CreatePostUiEvent()
    data class AddImages(val images: List<Uri>) : CreatePostUiEvent()
    data class SelectCategory(val category: String) : CreatePostUiEvent()
    data class ShowError(val message: String) : CreatePostUiEvent()
    data object ResetPostSuccess : CreatePostUiEvent()
}