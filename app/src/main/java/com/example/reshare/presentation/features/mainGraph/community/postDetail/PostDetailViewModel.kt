package com.example.reshare.presentation.features.mainGraph.community.postDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.domain.usecase.comment.AddCommentUseCase
import com.example.reshare.domain.usecase.comment.GetCommentsByPostUseCase
import com.example.reshare.domain.usecase.post.ToggleLike
import com.example.reshare.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getCommentsByPostUseCase : GetCommentsByPostUseCase,
    private val addCommentUseCase : AddCommentUseCase,
    private val toggleLike: ToggleLike
) : ViewModel() {
    private val _state = MutableStateFlow(PostDetailState())
    val state: StateFlow<PostDetailState> = _state

    fun onEvent(event: PostDetailUiEvent) {
        when (event) {
            is PostDetailUiEvent.LoadComments -> {
                loadComments(event.postId)
            }
            is PostDetailUiEvent.AddComment -> {
                addComment(event.content, event.postId)
            }
            is PostDetailUiEvent.ResetAddCommentState -> {
                _state.update {
                    it.copy(isAddingComment = false, addCommentError = null)
                }
            }
            is PostDetailUiEvent.SetPost -> {
                _state.update { it.copy(post = event.post) }
            }
            is PostDetailUiEvent.ToggleLike -> {
                togglePostLike(event.postId)
            }
        }
    }

    private fun loadComments(postId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = getCommentsByPostUseCase(postId)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            comments = result.data ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Unknown error"
                        )
                    }
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    private fun addComment(content: String, postId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isAddingComment = true, addCommentError = null) }

            when (val result = addCommentUseCase(content, postId)) {
                is Resource.Success -> {
                    val newComment = result.data
                    if (newComment != null) {
                        _state.update {
                            it.copy(
                                isAddingComment = false,
                                comments = it.comments + newComment,
                                highlightedCommentId = newComment.id,
                                post = it.post?.copy(
                                    commentsCount = it.post.commentsCount + 1
                                )
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isAddingComment = false,
                                addCommentError = "No comment returned"
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isAddingComment = false,
                            addCommentError = result.message ?: "Comment failed"
                        )
                    }
                }

                is Resource.Loading -> {
                    // Không cần xử lý, vì đã update loading ở đầu hàm rồi
                }
            }
        }
    }

    private fun togglePostLike(postId: String) {
        viewModelScope.launch {
            val result = toggleLike(postId)
            if (result is Resource.Success) {
                result.data?.let { like ->
                    _state.update { currentState ->
                        val updatedPost  = currentState.post?.takeIf { it.id == postId }?.copy(
                            likesCount = like.likes,
                            likedByCurrentUser = like.liked
                        )
                        currentState.copy(post = updatedPost)
                    }
                }
            }
        }
    }
}
