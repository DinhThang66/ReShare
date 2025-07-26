package com.example.reshare.presentation.utils

import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.User

object ChatClientManager {
    private var isConnected = false

    fun resetConnectionState() {
        isConnected = false
    }

    fun connectIfNeeded(userId: String, name: String, image: String, token: String) {
        val client = ChatClient.instance()

        if (!isConnected && client.clientState.user.value == null) {
            val user = User(
                id = userId,
                name = name, image = image
            )
            client.connectUser(user, token).enqueue {
                isConnected = it.isSuccess
            }
        }
    }
}