package com.example.reshare.presentation.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reshare.presentation.features.auth.login.LoginScreen
import com.example.reshare.presentation.features.auth.register.RegisterScreen
import com.example.reshare.presentation.features.auth.setYourLocation.SetYourLocationScreen
import com.example.reshare.presentation.features.main.MainScreen
import com.example.reshare.presentation.features.mainGraph.community.createPost.CreatePostScreen
import com.example.reshare.presentation.features.mainGraph.community.postDetail.PostDetailScreen
import com.example.reshare.presentation.features.mainGraph.community.userProfile.UserProfileScreen
import com.example.reshare.presentation.features.mainGraph.home.chooseALocation.ChooseALocationScreen
import com.example.reshare.presentation.features.mainGraph.home.giveAway.GiveAwayScreen
import com.example.reshare.presentation.features.mainGraph.home.itemDetail.ItemDetailScreen
import com.example.reshare.presentation.features.mainGraph.home.makeARequest.MakeRequestScreen
import com.example.reshare.presentation.features.mainGraph.messages.ChatScreen
import com.example.reshare.presentation.features.sideBar.myImpact.MyImpactScreen
import com.example.reshare.presentation.features.sideBar.myListing.MyListingsScreen
import com.example.reshare.presentation.features.sideBar.MyWatchListScreen
import com.example.reshare.presentation.features.sideBar.myProfile.MyProfileScreen
import com.example.reshare.presentation.utils.Screen
import com.example.reshare.ui.theme.ReShareTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainActivityViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(state.hasValidToken) {
                if (state.hasValidToken == true) {
                    viewModel.onEvent(MainActivityEvent.ConnectChatUserIfNeeded)
                }
            }

            ReShareTheme {
                when (state.hasValidToken) {
                    null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    false -> {
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
                    true -> {
                        when {
                            state.hasLocation == false -> {
                                SetYourLocationScreen()
                            }
                            state.hasLocation == true && state.isChatReady -> {
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
                                    composable(Screen.UserProfile.route + "/{userId}") {backStackEntry ->
                                        val userId = backStackEntry.arguments?.getString("userId") ?: return@composable
                                        UserProfileScreen(
                                            navController = navController,
                                            userId = userId
                                        )
                                    }
                                    animatedComposableHorizontalSlide(Screen.CreatePost.route) {
                                        CreatePostScreen(navController = navController)
                                    }
                                    animatedComposableVerticalSlide(Screen.RadiusMap.route) {
                                        ChooseALocationScreen(navController = navController)
                                    }
                                    animatedComposableHorizontalSlide(Screen.MakeARequest.route) {
                                        MakeRequestScreen(navController = navController)
                                    }

                                    composable(Screen.MyImpactScreen.route) {
                                        MyImpactScreen(navController = navController)
                                    }
                                    composable(Screen.MyWatchlistScreen.route) {
                                        MyWatchListScreen(navController = navController)
                                    }
                                    composable(Screen.MyListingsScreen.route) {
                                        MyListingsScreen(navController = navController)
                                    }
                                    composable(Screen.MyProfileScreen.route) {
                                        MyProfileScreen(navController = navController)
                                    }

                                    composable(Screen.GiveAway.route) {
                                        GiveAwayScreen(navController = navController)
                                    }
                                }
                            }
                            else -> {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



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

fun NavGraphBuilder.animatedComposableHorizontalSlide(
    route: String,
    content: @Composable () -> Unit
) {
    composable(
        route = route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // right in
                animationSpec = tween(durationMillis = 300)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth }, // left out
                animationSpec = tween(durationMillis = 300)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth }, // left in
                animationSpec = tween(durationMillis = 300)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth }, // right out
                animationSpec = tween(durationMillis = 300)
            )
        }
    ) {
        content()
    }
}