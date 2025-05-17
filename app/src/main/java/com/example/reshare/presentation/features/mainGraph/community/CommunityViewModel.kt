package com.example.reshare.presentation.features.mainGraph.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.domain.usecase.post.GetAllPostsUseCase
import com.example.reshare.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val getAllPostsUseCase : GetAllPostsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CommunityState())
    val state = _state.asStateFlow()

    init {
        loadPosts()
    }
    fun onEvent(event: CommunityUiEvent) {
        when (event) {
            is CommunityUiEvent.Refresh -> {
                loadPosts(forceRefresh = true)
            }
        }
    }

    private fun loadPosts(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getAllPostsUseCase(
                forceFetchFromRemote = forceRefresh
            ).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _state.update {
                        it.copy(isLoading = result.isLoading)
                    }

                    is Resource.Success -> result.data?.let { posts ->
                        _state.update {
                            it.copy(posts = posts, isLoading = false)
                        }
                    }

                    is Resource.Error -> _state.update {
                        it.copy(isLoading = false, error = result.message)
                    }
                }
            }
        }
    }

}









/*
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

 */