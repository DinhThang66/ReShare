package com.example.reshare.presentation.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.data.local.UserPreferences
import com.example.reshare.domain.usecase.auth.GetStreamTokenUseCase
import com.example.reshare.presentation.utils.ChatClientManager
import com.example.reshare.presentation.utils.isTokenExpired
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val getStreamTokenUseCase: GetStreamTokenUseCase
) : ViewModel() {

    val hasValidToken = userPreferences.userToken.map { token ->
        !token.isNullOrBlank() && !isTokenExpired(token)
    }

    fun connectChatUserIfNeeded() {
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