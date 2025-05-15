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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.Notifications
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.reshare.R
import com.example.reshare.presentation.navigation.BottomNavBar
import com.example.reshare.presentation.navigation.BottomNavGraph
import com.example.reshare.presentation.utils.bottomNavigationItemsList
import com.example.reshare.ui.theme.LightPurple
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
                "home" -> "Good morning"
                else -> bottomNavigationItemsList.find { it.route == currentRoute }
                    ?.title ?: bottomNavigationItemsList.first().title
            }
        }
    }
    val topBarColor by remember(currentRoute) {
        derivedStateOf {
            when (currentRoute) {
                "home", "explore" -> LightPurple
                else -> Color.White
            }
        }
    }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val name by viewModel.userName.collectAsState(initial = "")

    ModalNavigationDrawer(
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .width(screenWidth * 0.8f),
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
                            Text("$name", fontSize = 18.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        }
                        @Suppress("DEPRECATION")
                        Icon(Icons.Default.ArrowForwardIos, null, modifier = Modifier.size(16.dp))
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(12.dp))

                    DrawerItem(icon = Icons.Default.Home, title = "Home")
                    DrawerItem(icon = Icons.Default.AccountCircle, title = "My Impact")
                    DrawerItem(icon = Icons.Default.Verified, title = "My Badges")
                    DrawerItem(icon = Icons.Default.EmojiEvents, title = "My Levels")
                    DrawerItem(icon = Icons.Default.Flag, title = "Goals")
                    DrawerItem(icon = Icons.Default.StarBorder, title = "My Watchlist")
                    @Suppress("DEPRECATION")
                    DrawerItem(icon = Icons.Default.List, title = "My Listings")
                    DrawerItem(icon = Icons.Default.Person, title = "Profile")
                }
            }
        },
        drawerState = drawerState,
    ) {
        Scaffold(
            topBar = {
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
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
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
fun DrawerItem(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .clickable { /* Handle click */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null)
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, fontSize = 16.sp)
    }
}