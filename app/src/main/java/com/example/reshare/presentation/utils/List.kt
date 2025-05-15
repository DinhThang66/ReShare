package com.example.reshare.presentation.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import com.example.reshare.presentation.navigation.NavigationItem

val bottomNavigationItemsList = listOf(
    NavigationItem(
        title = "Home",
        route = Screen.Home.route,
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home,
    ),
    NavigationItem(
        title = "Explore",
        route = Screen.Explore.route,
        selectedIcon = Icons.Filled.Search,
        unSelectedIcon = Icons.Outlined.Search,
    ),
    NavigationItem(
        title = "Community",
        route = Screen.Community.route,
        selectedIcon = Icons.Filled.Forum,
        unSelectedIcon = Icons.Outlined.Forum,
    ),
    NavigationItem(
        title = "Messages",
        route = Screen.Messages.route,
        selectedIcon = Icons.Filled.ChatBubble,
        unSelectedIcon = Icons.Filled.ChatBubbleOutline,
    ),
)

val categories = listOf(
    "All", "ReShare Announcements", "App Q&A", "Spreading The Word",
    "Zero Waste", "ReShare Love", "Recipes"
)

val sectionTitles = listOf(
    "New Food Free",
    "New non-food listings",
    "Save these from going to waste",
    "New reduced food near you"
)