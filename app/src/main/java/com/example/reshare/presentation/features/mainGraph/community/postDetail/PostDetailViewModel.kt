package com.example.reshare.presentation.features.mainGraph.community.postDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.domain.model.Comment
import com.example.reshare.domain.usecase.comment.AddCommentUseCase
import com.example.reshare.domain.usecase.comment.GetCommentsByPostUseCase
import com.example.reshare.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getCommentsByPostUseCase : GetCommentsByPostUseCase,
    private val addCommentUseCase : AddCommentUseCase
) : ViewModel() {
    // Lưu danh sách comment hiện tại
    private val _comments = mutableListOf<Comment>()

    // StateFlow cho UI
    private val _commentsState = MutableStateFlow<Resource<List<Comment>>>(Resource.Loading())
    val commentsState: StateFlow<Resource<List<Comment>>> = _commentsState

    private val _addCommentState = MutableStateFlow<Resource<Unit>?>(null)
    val addCommentState: StateFlow<Resource<Unit>?> = _addCommentState

    private val _highlightedCommentId = MutableStateFlow<String?>(null)
    val highlightedCommentId: StateFlow<String?> = _highlightedCommentId

    fun getComments(postId: String) {
        viewModelScope.launch {
            _commentsState.value = Resource.Loading()
            val result = getCommentsByPostUseCase(postId)
            result
                .onSuccess { comments ->
                    _comments.clear()
                    _comments.addAll(comments)
                    _commentsState.value = Resource.Success(_comments.toList())
                }
                .onFailure { throwable ->
                    _commentsState.value = Resource.Error(throwable.message ?: "Unknown error")
                }
        }
    }

    fun addComment(content: String, postId: String) {
        viewModelScope.launch {
            _addCommentState.value = Resource.Loading()
            val result = addCommentUseCase(content, postId)
            result.onSuccess { newComment ->
                _addCommentState.value = Resource.Success(Unit)
                _comments.add(newComment) // chỉ thêm comment mới
                _commentsState.value = Resource.Success(_comments.toList()) // cập nhật UI
                _highlightedCommentId.value = newComment.id                 // đánh dấu comment mới
            }.onFailure {
                _addCommentState.value = Resource.Error(it.message ?: "Comment failed")
            }
        }
    }

    fun resetAddCommentState() {
        _addCommentState.value = null
    }
}