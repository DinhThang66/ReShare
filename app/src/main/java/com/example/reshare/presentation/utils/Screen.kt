package com.example.reshare.presentation.utils

sealed class Screen(var route: String) {
    data object  Main: Screen("Main")

    data object Home: Screen("Home")
    data object Explore: Screen("Explore")
    data object Community: Screen("Community")
    data object Messages: Screen("Messages")
    data object PostDetail: Screen("PostDetail")

    data object Login: Screen("Login")
    data object Register: Screen("Register")
    data object ItemDetail: Screen("ItemDetail")
    data object UserProfile: Screen("UserProfile")
    data object CreatePost: Screen("CreatePost")
    data object RadiusMap: Screen("RadiusMap")
    data object MakeARequest: Screen("MakeARequest")

    data object MyImpactScreen: Screen("MyImpact")
    data object MyWatchlistScreen: Screen("MyWatchlist")
    data object MyListingsScreen: Screen("MyListings")
    data object MyProfileScreen: Screen("MyProfile")
    data object SetYourLocationScreen: Screen("SetYourLocation")
}