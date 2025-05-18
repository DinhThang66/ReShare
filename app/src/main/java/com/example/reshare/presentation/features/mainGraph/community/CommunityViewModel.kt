package com.example.reshare.presentation.features.mainGraph.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.domain.usecase.post.GetPostsUseCase
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
    private val getPostsUseCase : GetPostsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CommunityState())
    val state = _state.asStateFlow()

    init {
        loadPosts()
    }
    fun onEvent(event: CommunityUiEvent) {
        when (event) {
            is CommunityUiEvent.Refresh -> {
                loadPosts(page = 1)
            }
            is CommunityUiEvent.LoadNextPage -> {
                if (!state.value.isPaginating && !state.value.isLastPage) {
                    loadPosts(page = state.value.currentPage + 1)
                }
            }
        }
    }

    private fun loadPosts(page: Int = 1, forceRefresh: Boolean = true) {
        viewModelScope.launch {
            // Set loading flags
            _state.update {
                if (page == 1) {
                    it.copy(isInitialLoading = true, error = null)
                } else {
                    it.copy(isPaginating = true)
                }
            }

            getPostsUseCase(
                forceFetchFromRemote = forceRefresh,
                page = page
            ).collectLatest { result ->
                when (result) {
                    is Resource.Success -> result.data?.let { pagedResult  ->
                        val newPosts = pagedResult.data
                        val allPosts = if (page == 1) newPosts else state.value.posts + newPosts

                        val isLastPage = pagedResult.currentPage >= pagedResult.totalPages

                        _state.update {
                            it.copy(
                                posts = allPosts,
                                currentPage = pagedResult.currentPage,
                                isInitialLoading = false,
                                isPaginating = false,
                                isLastPage = isLastPage
                            )
                        }
                    }
                    is Resource.Error -> _state.update {
                        it.copy(isInitialLoading = false, isPaginating = false, error = result.message)
                    }
                    else -> {}
                }
            }
        }
    }
}
