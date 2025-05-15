package com.example.reshare.presentation.features.mainGraph.messages

import android.app.Activity
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

@Composable
fun ChatScreen(
    channelId: String,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    LaunchedEffect(Unit) {
        @Suppress("DEPRECATION")
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    ChatTheme {
        io.getstream.chat.android.compose.ui.messages.MessagesScreen(
            viewModelFactory = MessagesViewModelFactory(
                context = context,
                channelId = channelId,
                messageLimit = 30
            ),
            onBackPressed = onBackPressed
        )
    }
}