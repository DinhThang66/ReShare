package com.example.reshare.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.reshare.ui.theme.DarkPurple

@Composable
fun BottomNavBar(
    items: List<NavigationItem>,
    bottomNavController: NavHostController
) {
    val navStackBackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { item ->
                AddItem(
                    item = item,
                    currentDestination = currentDestination,
                    navController = bottomNavController
                )

            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    item: NavigationItem,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

    val background =
        if (selected) DarkPurple else Color.Transparent
    val contentColor =
        if (selected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .weight(1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (!selected) {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id)  {
                            saveState = true
                        }
                        launchSingleTop = true          // Không tạo lại màn hình nếu đang ở đó
                        restoreState = true             // Khôi phục lại trạng thái trước (scroll...)
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .height(40.dp)
                .clip(CircleShape)
                .background(background)

        ) {
            Row(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    imageVector = if(selected) item.selectedIcon else item.unSelectedIcon,
                    contentDescription = null,
                    tint = contentColor
                )
            }
        }
    }
}
