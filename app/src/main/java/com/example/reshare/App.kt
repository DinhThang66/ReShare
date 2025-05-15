package com.example.reshare

import android.app.Application
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dagger.hilt.android.HiltAndroidApp
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        // Khởi tạo một lần duy nhất
        // 1 - Set up the OfflinePlugin for offline storage
        val offlinePluginFactory = StreamOfflinePluginFactory(
            appContext = this
        )
        val statePluginFactory = StreamStatePluginFactory(
            config = StatePluginConfig(),
            appContext = this
        )
        // 2 - Set up the client for API calls and with the plugin for offline storage
        val client = ChatClient.Builder(
            apiKey = getString(R.string.stream_api_key),
            appContext = this
        )
            .withPlugins(offlinePluginFactory, statePluginFactory)
            .logLevel(ChatLogLevel.ALL)
            .build()
    }
}