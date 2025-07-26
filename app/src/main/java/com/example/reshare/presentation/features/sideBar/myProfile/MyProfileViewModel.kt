package com.example.reshare.presentation.features.sideBar.myProfile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshare.data.local.UserPreferences
import com.example.reshare.domain.model.User
import com.example.reshare.presentation.utils.ChatClientManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _state = MutableStateFlow(MyProfileState())
    val state = _state.asStateFlow()

    init {
        setValue()
    }

    fun onEvent(event: MyProfileUiEvent) {
        when (event) {
            is MyProfileUiEvent.Logout -> {
                viewModelScope.launch {
                    userPreferences.clearUser()
                    ChatClient.instance().disconnect(flushPersistence = true).enqueue {
                        // onSuccess: navigate to Login or other cleanup if needed
                        ChatClientManager.resetConnectionState()
                    }
                }
            }
        }
    }


    private fun setValue() {
        viewModelScope.launch {
            val userId = userPreferences.userId.firstOrNull()
            val firstName = userPreferences.userFName.firstOrNull()
            val image = userPreferences.userProfilePic.firstOrNull()

            _state.update {
                it.copy(user = User(
                    id = userId ?: "",
                    firstName = firstName ?: "",
                    lastName = "",
                    email = "",
                    profilePic = image ?: "",
                    latitude = 0.0,
                    longitude = 0.0,
                    radius = 0f
                ))
            }
        }
    }
}