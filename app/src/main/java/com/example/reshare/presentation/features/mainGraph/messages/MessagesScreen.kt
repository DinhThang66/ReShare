package com.example.reshare.presentation.features.mainGraph.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.compose.ui.channels.list.ChannelList
import io.getstream.chat.android.models.InitializationState

@Composable
fun MessagesScreen(
    innerPadding: PaddingValues,
    navController: NavHostController,
) {
    val client = ChatClient.instance()
    val clientInitializationState by client.clientState.initializationState.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(innerPadding)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //val clientInitialisationState by client.clientState.initializationState.collectAsState()
        io.getstream.chat.android.compose.ui.theme.ChatTheme {
            when (clientInitializationState) {
                InitializationState.COMPLETE -> {
                    ChannelList(
                        onChannelClick = { channel ->
                            navController.navigate("messages/${channel.cid}")
                        },
                    )
                }
                InitializationState.INITIALIZING -> {
                    //Text(text = "Initialising...")
                }
                InitializationState.NOT_INITIALIZED -> {
                    //Text(text = "Not initialized...")
                }
            }
        }
    }

}