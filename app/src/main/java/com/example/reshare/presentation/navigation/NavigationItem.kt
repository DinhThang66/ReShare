package com.example.reshare.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title : String,
    val route : String,
    val selectedIcon : ImageVector,
    val unSelectedIcon : ImageVector,
)