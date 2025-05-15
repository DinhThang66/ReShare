package com.example.reshare.presentation.utils

sealed class Screen(var route: String) {
    data object  Main: Screen("main")

    data object Home: Screen("home")
    data object Explore: Screen("explore")
    data object Community: Screen("community")
    data object Messages: Screen("messages")
    data object PostDetail: Screen("postDetail")

    data object Login: Screen("login")
    data object Register: Screen("register")
    data object ItemDetail: Screen("itemDetail")
}