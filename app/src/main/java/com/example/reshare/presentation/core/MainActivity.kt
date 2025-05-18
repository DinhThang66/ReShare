package com.example.reshare.presentation.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reshare.presentation.features.auth.login.LoginScreen
import com.example.reshare.presentation.features.auth.register.RegisterScreen
import com.example.reshare.presentation.features.main.MainScreen
import com.example.reshare.presentation.features.mainGraph.community.postDetail.PostDetailScreen
import com.example.reshare.presentation.features.mainGraph.home.itemDetail.ItemDetailScreen
import com.example.reshare.presentation.features.mainGraph.messages.ChatScreen
import com.example.reshare.presentation.utils.Screen
import com.example.reshare.ui.theme.ReShareTheme
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.InitializationState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainActivityViewModel = hiltViewModel()
            val hasValidToken by viewModel.hasValidToken.collectAsState(initial = null)

            // lấy trạng thái khởi tạo của ChatClient
            val clientState = ChatClient.instance().clientState
            val isChatReady = clientState.initializationState.collectAsState().value == InitializationState.COMPLETE

            LaunchedEffect(hasValidToken) {
                if (hasValidToken == true) {
                    viewModel.connectChatUserIfNeeded()
                }
            }

            ReShareTheme {
                when {
                    hasValidToken == false -> {
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Login.route
                        ) {
                            animatedComposable(Screen.Login.route) {
                                LoginScreen(navController = navController)
                            }
                            animatedComposableVerticalSlide(Screen.Register.route) {
                                RegisterScreen(navController = navController)
                            }
                        }
                    }

                    hasValidToken == true && isChatReady -> {
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Main.route
                        ) {
                            composable(Screen.Main.route) {
                                MainScreen(navController = navController)
                            }
                            composable(Screen.PostDetail.route) {
                                PostDetailScreen(navController = navController)
                            }
                            animatedComposableHorizontalSlide(Screen.ItemDetail.route) {
                                ItemDetailScreen(navController = navController)
                            }
                            composable(Screen.Messages.route + "/{channelId}") { backStackEntry ->
                                val channelId = backStackEntry.arguments?.getString("channelId") ?: return@composable
                                ChatScreen(
                                    channelId = channelId,
                                    onBackPressed = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }
                    }

                    !isChatReady -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}










@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.animatedComposable(
    route: String,
    content: @Composable () -> Unit
) {
    composable(
        route = route,
        enterTransition = { fadeIn(animationSpec = tween(0)) },
        exitTransition = { fadeOut(animationSpec = tween(0)) },
        popEnterTransition = { fadeIn(animationSpec = tween(0)) },
        popExitTransition = { fadeOut(animationSpec = tween(0)) }
    ) { content() }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.animatedComposableVerticalSlide(
    route: String,
    content: @Composable () -> Unit
) {
    composable(
        route = route,
        enterTransition = {
            slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 300)
            )
        },
        exitTransition = {
            slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 300)
            )
        },
        popEnterTransition = {
            slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 300)
            )
        },
        popExitTransition = {
            slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 300)
            )
        }
    ) {
        content()
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.animatedComposableHorizontalSlide(
    route: String,
    content: @Composable () -> Unit
) {
    composable(
        route = route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // từ phải vào
                animationSpec = tween(durationMillis = 300)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth }, // sang trái
                animationSpec = tween(durationMillis = 300)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth }, // từ trái vào
                animationSpec = tween(durationMillis = 300)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth }, // sang phải
                animationSpec = tween(durationMillis = 300)
            )
        }
    ) {
        content()
    }
}


/*
if (hasValidToken != null) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        //startDestination = Screen.Login.route
        startDestination = if (hasValidToken == true) Screen.Main.route else Screen.Login.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController = navController)
        }
        animatedComposable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        animatedComposableVerticalSlide(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(Screen.PostDetail.route) {
            PostDetailScreen(navController = navController)
        }
        animatedComposableHorizontalSlide(Screen.ItemDetail.route) {
            ItemDetailScreen(navController = navController)
        }
        composable(Screen.Messages.route + "/{channelId}") { backStackEntry ->
            val channelId = backStackEntry.arguments?.getString("channelId") ?: return@composable
            ChatScreen(
                channelId = channelId,
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
} else {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
 */


