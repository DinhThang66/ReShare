package com.example.reshare.presentation.features.mainGraph.community

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.domain.model.Post
import com.example.reshare.domain.usecase.post.GetAllPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val getAllPostsUseCase : GetAllPostsUseCase
) : ViewModel() {
    private val _postsState = mutableStateOf<UiState<List<Post>>>(UiState.Loading)
    val postsState: State<UiState<List<Post>>> = _postsState

    init {
        getAllPosts()
    }

    private fun getAllPosts() {
        viewModelScope.launch {
            _postsState.value = UiState.Loading
            val result = getAllPostsUseCase()
            _postsState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

}