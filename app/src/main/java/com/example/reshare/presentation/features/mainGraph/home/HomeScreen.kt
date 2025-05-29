package com.example.reshare.presentation.features.mainGraph.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.reshare.domain.model.Product
import com.example.reshare.presentation.components.HomeItemCard
import com.example.reshare.presentation.features.mainGraph.community.CommunityViewModel
import com.example.reshare.presentation.utils.Screen
import com.example.reshare.presentation.utils.sectionTitles
import com.example.reshare.ui.theme.LightPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(innerPadding)
    ) {
        // Location on
        LocationOn(navController = navController)

        // Loading
        if (state.isLoading) {
           CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
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
        }
    }
}

@Composable
fun LocationOn(
    navController: NavHostController,
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
                text = "Hải Phòng",
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
            text = "Listings within 10km",
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