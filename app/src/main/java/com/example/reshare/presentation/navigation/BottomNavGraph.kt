package com.example.reshare.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.reshare.presentation.features.mainGraph.community.CommunityScreen
import com.example.reshare.presentation.features.mainGraph.explore.ExploreScreen
import com.example.reshare.presentation.features.mainGraph.home.HomeScreen
import com.example.reshare.presentation.features.mainGraph.messages.MessagesScreen
import com.example.reshare.presentation.utils.Screen

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    bottomNavController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = bottomNavController,
        startDestination = Screen.Home.route
    ){
        animatedComposable(Screen.Home.route){
            HomeScreen(innerPadding = innerPadding, navController = navController)
        }
        animatedComposable(Screen.Explore.route){
            ExploreScreen(innerPadding = innerPadding, navController = navController)
        }
        animatedComposable(Screen.Community.route){
            CommunityScreen(innerPadding = innerPadding, navController = navController)
        }
        animatedComposable(Screen.Messages.route){
            MessagesScreen(innerPadding = innerPadding, navController = navController)
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