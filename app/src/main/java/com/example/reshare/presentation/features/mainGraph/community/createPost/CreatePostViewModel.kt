package com.example.reshare.presentation.features.mainGraph.community.createPost

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.data.local.UserPreferences
import com.example.reshare.domain.usecase.post.CreatePostUseCase
import com.example.reshare.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val createPostUseCase: CreatePostUseCase,
    @ApplicationContext private val appContext: Context // cần truyền context để đọc ảnh từ Uri
) : ViewModel() {
    private val _state = MutableStateFlow(CreatePostState())
    val state: StateFlow<CreatePostState> = _state

    init {
        viewModelScope.launch {
            userPreferences.getUserFlow().collect { user ->
                _state.update { it.copy(user = user) }
            }
        }
    }

    fun onEvent(event: CreatePostUiEvent) {
        when (event) {
            is CreatePostUiEvent.UpdateContent -> {
                _state.update { it.copy(postContent = event.text) }
            }
            is CreatePostUiEvent.AddImages -> {
                val images = event.images.take(2)
                _state.update { it.copy(selectedImages = images) }
            }
            is CreatePostUiEvent.SelectCategory -> {
                _state.update { it.copy(selectedCategory = event.category) }
            }
            is CreatePostUiEvent.SubmitPost -> {
                submitPost()
            }
            is CreatePostUiEvent.ShowError -> {
                _state.update { it.copy(error = event.message) }
            }
            is CreatePostUiEvent.ResetPostSuccess -> {
                _state.update { it.copy(postSuccess = false) }
            }
        }
    }

    private fun submitPost() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.postContent.isBlank()) {
                _state.update { it.copy(error = "Content cannot be blank") }
                return@launch
            }

            _state.update { it.copy(isSubmitting = true, error = null) }

            try {
                val imageParts = currentState.selectedImages.mapIndexed { index, uri ->
                    val inputStream = appContext.contentResolver.openInputStream(uri)
                    val bytes = inputStream?.readBytes() ?: byteArrayOf()
                    inputStream?.close()

                    val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData(
                        name = "images",
                        filename = "image_${System.currentTimeMillis()}_$index.jpg",
                        body = requestBody
                    )
                }

                when (val result = createPostUseCase(currentState.postContent, imageParts)) {
                    is Resource.Success -> {
                        _state.update { it.copy(isSubmitting = false, postSuccess = true) }
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isSubmitting = false, error = result.message ?: "Error!!!") }
                    }
                    else -> {
                        _state.update { it.copy(isSubmitting = false, error = "Unknown error") }
                    }
                }

            } catch (e: Exception) {
                _state.update { it.copy(isSubmitting = false, error = "Error while posting: ${e.localizedMessage}") }
            }
        }
    }
}