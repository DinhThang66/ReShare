package com.example.reshare.presentation.features.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.reshare.R
import com.example.reshare.presentation.navigation.BottomNavBar
import com.example.reshare.presentation.navigation.BottomNavGraph
import com.example.reshare.presentation.utils.Screen
import com.example.reshare.presentation.utils.bottomNavigationItemsList
import com.example.reshare.ui.theme.LightPurple
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currentRoute by remember(navBackStackEntry) {
        derivedStateOf { navBackStackEntry?.destination?.route }
    }

    val topBarTitle by remember(currentRoute) {
        derivedStateOf {
            when (currentRoute) {
                Screen.Home.route -> "Good morning"
                else -> bottomNavigationItemsList.find { it.route == currentRoute }
                    ?.title ?: bottomNavigationItemsList.first().title
            }
        }
    }
    val topBarColor by remember(currentRoute) {
        derivedStateOf {
            when (currentRoute) {
                Screen.Home.route, Screen.Explore.route -> LightPurple
                else -> Color.White
            }
        }
    }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val name by viewModel.userName.collectAsState(initial = "")

    ModalNavigationDrawer(
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            MainDrawerContent(
                name = name ?: "Guest",
                screenWidth = screenWidth,
                navController = navController,
                drawerState = drawerState,
                scope = scope,
                currentRoute = currentRoute
            )
        },
        drawerState = drawerState,
    ) {
        Scaffold(
            topBar = {
                MainTopBar(
                    topBarTitle = topBarTitle,
                    topBarColor = topBarColor,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onNotificationClick = {}
                )
            },
            bottomBar = {
                BottomNavBar(
                    items = bottomNavigationItemsList,
                    bottomNavController = bottomNavController
                )
            }
        ) { innerPadding ->
            BottomNavGraph(
                navController = navController,
                bottomNavController = bottomNavController,
                innerPadding = innerPadding
            )
        }
    }
}




@Composable
fun DrawerItem(
    icon: ImageVector, title: String, active: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (active) Color(0xFFEC4899) else Color.Black
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
            color = if (active) Color(0xFFEC4899) else Color.Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    topBarTitle: String,
    topBarColor: Color,
    onMenuClick: () -> Unit,
    onNotificationClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = topBarTitle,
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )},
        colors = TopAppBarDefaults.topAppBarColors(topBarColor),
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = onNotificationClick) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    )
}

@Composable
fun MainDrawerContent(
    name: String,
    screenWidth: Dp,
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    currentRoute: String?
) {
    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .width(screenWidth * 0.8f),
        drawerContainerColor = Color.White
    ) {

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.img),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(name, fontSize = 18.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                }
                @Suppress("DEPRECATION")
                Icon(Icons.Default.ArrowForwardIos, null, modifier = Modifier.size(16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            DrawerItem(
                icon = Icons.Default.Home, title = "Home", active = true,
                onClick = { }
            )
            DrawerItem(
                icon = Icons.Default.AccountCircle, title = "My Impact",
                onClick = {
                    navController.navigate(Screen.MyImpactScreen.route) {
                        launchSingleTop = true
                    }
                    scope.launch { drawerState.close() }
                }
            )
            DrawerItem(
                icon = Icons.Default.StarBorder, title = "My Watchlist",
                onClick = {
                    navController.navigate(Screen.MyWatchlistScreen.route) {
                        launchSingleTop = true
                    }
                    scope.launch { drawerState.close() }
                }
            )
            @Suppress("DEPRECATION")
            DrawerItem(
                icon = Icons.Default.List, title = "My Listings",
                onClick = {
                    navController.navigate(Screen.MyListingsScreen.route) {
                        launchSingleTop = true
                    }
                    scope.launch { drawerState.close() }
                }
            )
            DrawerItem(
                icon = Icons.Default.Person, title = "Profile",
                onClick = {
                    navController.navigate(Screen.MyProfileScreen.route) {
                        launchSingleTop = true
                    }
                    scope.launch { drawerState.close() }
                }
            )
        }
    }
}