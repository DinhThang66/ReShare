package com.example.reshare.presentation.features.mainGraph.community.userProfile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.data.local.UserPreferences
import com.example.reshare.domain.usecase.user.GetUserUseCase
import com.example.reshare.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _state = MutableStateFlow(UserProfileState())
    val state: StateFlow<UserProfileState> = _state

    fun onEvent(event: UserProfileUiEvent) {
        when (event) {
            is UserProfileUiEvent.GetUser -> {
                getUser(event.userId)
            }
            is UserProfileUiEvent.MessageClicked -> {
                createChatChannel(event.userId)
            }
            is UserProfileUiEvent.ClearNavigation -> {
                clearChannelNavigation()
            }
        }
    }

    private fun getUser(userId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when(val result = getUserUseCase(userId)){
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            user = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Resource.Error-> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Unknown error"
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun createChatChannel(targetUserId: String) {
        viewModelScope.launch {
            val client = ChatClient.instance()
            val myUserId = userPreferences.userId.firstOrNull()

            if (myUserId == null) {
                _state.update { it.copy(error = "Current user not found") }
                return@launch
            }
            val channelId = listOf(myUserId, targetUserId).sorted().joinToString("_")

            client.createChannel(
                channelType = "messaging",
                channelId = channelId,
                memberIds = listOf(targetUserId, myUserId),
                extraData = emptyMap()
            ).enqueue { result ->
                if (result.isSuccess) {
                    val channel = result.getOrNull()

                    _state.update {
                        it.copy(createdChannelId = "${channel?.cid}")
                    }
                } else {
                    _state.update {
                        it.copy(error = result.errorOrNull()?.message ?: "Unable to create channel")
                    }
                }
            }
        }
    }

    private fun clearChannelNavigation() {
        _state.update { it.copy(createdChannelId = null) }
    }
}