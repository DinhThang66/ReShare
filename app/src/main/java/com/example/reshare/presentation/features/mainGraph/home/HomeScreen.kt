package com.example.reshare.presentation.features.mainGraph.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.outlined.EmojiPeople
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.reshare.domain.model.Product
import com.example.reshare.presentation.components.HomeItemCard
import com.example.reshare.presentation.utils.Screen
import com.example.reshare.presentation.utils.categories
import com.example.reshare.ui.theme.DarkPurple
import com.example.reshare.ui.theme.LightPurple
import com.example.reshare.ui.theme.YellowD

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { viewModel.onEvent(HomeUiEvent.Refresh) }
    )
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    // Bắt sự kiện thay đổi lng, lat, radius từ chooseALocation
    val currentBackStackEntry = navController.currentBackStackEntry
    val savedStateHandle = currentBackStackEntry?.savedStateHandle
    LaunchedEffect(savedStateHandle) {
        savedStateHandle?.getLiveData<Boolean>("refresh")?.observeForever { shouldRefresh ->
            if (shouldRefresh == true) {
                viewModel.onEvent(HomeUiEvent.Refresh)
                savedStateHandle["refresh"] = false // Reset flag để tránh gọi lại
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ) {
            // Location on
            LocationOn(
                navController = navController,
                userLocation = state.userLocation,
                radius = state.radius
            )

            // Loading
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )
            }

            // LazyColumn
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize() // Or .weight(1f)
                    .background(Color.White),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                if (state.freeFood.isNotEmpty()) {
                    item {
                        Section(
                            title = "New Food Free",
                            products = state.freeFood,
                            navController = navController
                        )
                    }
                }

                if (state.nonFood.isNotEmpty()) {
                    item {
                        Section(
                            title = "New non-food listings",
                            products = state.nonFood,
                            navController = navController
                        )
                    }
                }

                if (state.reducedFood.isNotEmpty()) {
                    item {
                        Section(
                            title = "🔥 New reduced food near you",
                            products = state.reducedFood,
                            navController = navController
                        )
                    }
                }

                if (state.want.isNotEmpty()) {
                    item {
                        Section(
                            title = "🎯 Help a neighbour",
                            products = state.want,
                            navController = navController
                        )
                    }
                }
                if (!state.isLoading) {
                    item {
                        Button(
                            onClick = { showSheet = true },
                            modifier = Modifier
                                .padding(16.dp),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkPurple,
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "Create Item")
                        }
                    }
                }
            }
        }

        if (showSheet) {
            BottomSheet(
                sheetState = sheetState,
                onDismiss = { showSheet = false },
                navController = navController
            )
        }

        // Pull-to-refresh indicator
        PullRefreshIndicator(
            refreshing = state.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}





@Composable
fun LocationOn(
    navController: NavHostController,
    userLocation: String,
    radius: Float
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightPurple)
            .padding(horizontal = 16.dp)
            .padding(bottom = 10.dp)
            .clickable { navController.navigate(Screen.RadiusMap.route) },
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = "Location",
                tint = Color.Black,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = userLocation,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = "Dropdown",
                tint = Color.Black,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = "Listings within $radius km",
            fontWeight = FontWeight.Light,
            fontSize = 12.sp,
            color = Color.Black,
            modifier = Modifier
                .padding(start = 18.dp)
                .offset(y = (-4).dp)
        )
    }
}

@Composable
fun Section(
    title: String,
    products: List<Product>,
    navController: NavHostController
) {
    Column {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = title,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "All",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )
            @Suppress("DEPRECATION")
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 8.dp)
        ) {
            items(products) { product ->
                HomeItemCard(
                    product = product,
                    navController = navController
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    navController: NavHostController,
    sheetState: SheetState,
    onDismiss: () -> Unit
){
    LaunchedEffect(Unit) {
        sheetState.expand()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White,
        dragHandle = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            BottomSheetItem(
                onClick = { navController.navigate(Screen.GiveAway.route) },
                icon = Icons.Default.VolunteerActivism,
                backgroundIcon = DarkPurple,
                title = "Give away",
                subtitle = "Give away item"
            )
            BottomSheetItem(
                onClick = {},
                icon = Icons.Outlined.EmojiPeople,
                backgroundIcon = YellowD,
                title = "Wanted",
                subtitle = "Ask for something"
            )
        }
    }
}

@Composable
fun BottomSheetItem(
    onClick: () -> Unit,
    icon: ImageVector,
    backgroundIcon: Color,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundIcon),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider()
}