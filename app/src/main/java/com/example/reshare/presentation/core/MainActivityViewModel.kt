package com.example.reshare.presentation.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.data.local.UserPreferences
import com.example.reshare.domain.usecase.auth.GetStreamTokenUseCase
import com.example.reshare.presentation.features.auth.login.LoginUiEvent
import com.example.reshare.presentation.utils.ChatClientManager
import com.example.reshare.presentation.utils.isTokenExpired
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.InitializationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val getStreamTokenUseCase: GetStreamTokenUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MainActivityState())
    val state = _state.asStateFlow()

    init {
        observeUserState()
        observeChatInitialization()
    }

    fun onEvent(event: MainActivityEvent) {
        when (event) {
            is MainActivityEvent.ConnectChatUserIfNeeded -> connectChatUserIfNeeded()
        }
    }

    private fun observeUserState() {
        viewModelScope.launch {
            combine(
                userPreferences.userToken,
                userPreferences.hasLocation
            ) { token: String?, location: Boolean ->
                val validToken = !token.isNullOrBlank() && !isTokenExpired(token)
                Pair(validToken, location)
            }.collect { (validToken, location) ->
                _state.update {
                    it.copy(hasValidToken = validToken, hasLocation = location)
                }
            }
        }
    }
    private fun observeChatInitialization() {
        viewModelScope.launch {
            ChatClient.instance()
                .clientState
                .initializationState
                .collect { state ->
                    _state.update {
                        it.copy(isChatReady = state == InitializationState.COMPLETE)
                    }
                }
        }
    }

    private fun connectChatUserIfNeeded() {
        viewModelScope.launch {
            val userId = userPreferences.userId.firstOrNull()
            val name = userPreferences.userFName.firstOrNull()
            val image = userPreferences.userProfilePic.firstOrNull()
            val jwt = userPreferences.userToken.firstOrNull()
            var streamToken = userPreferences.streamToken.firstOrNull()

            if (userId.isNullOrBlank() || jwt.isNullOrBlank()) return@launch

            // Nếu chưa có streamToken → gọi API
            if (streamToken.isNullOrBlank()) {
                val result = getStreamTokenUseCase()
                if (result.isSuccess) {
                    streamToken = result.getOrNull()
                    streamToken?.let { userPreferences.saveStreamToken(it) }
                }
            }

            // Nếu có streamToken rồi → connect
            streamToken?.let {
                ChatClientManager.connectIfNeeded(
                    userId = userId,
                    name = name ?: "",
                    image = image ?: "",
                    token = it
                )
            }
        }
    }
}