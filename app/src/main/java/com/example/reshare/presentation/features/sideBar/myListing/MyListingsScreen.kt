package com.example.reshare.presentation.features.sideBar.myListing

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.reshare.R
import com.example.reshare.domain.model.Product
import com.example.reshare.presentation.components.ExploreListItem
import com.example.reshare.presentation.utils.Screen
import com.example.reshare.presentation.utils.capitalizeFirst
import com.example.reshare.presentation.utils.getBadgeStyle

@Composable
fun MyListingsScreen(
    navController: NavController,
    viewModel: MyListingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopBarCustom(onClick = { navController.popBackStack() }) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.error.isNotBlank() ->{
                    Text("Lỗi: ${state.error}", color = Color.Red)
                }
                else -> {
                    LazyColumn {
                        items(state.products) {
                            Item(
                                navController = navController,
                                product = it,
                                rating = 5.0f
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarCustom(
    onClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "My Listing",
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onClick
            ) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }
        },
        modifier = Modifier.shadow(2.dp)
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun Item(
    navController: NavController,
    product: Product,
    rating: Float?
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .height(110.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row {
            Box(modifier = Modifier.width(110.dp).fillMaxHeight()){
                // Left Image
                AsyncImage(
                    model = product.images[0],
                    contentDescription = "Product Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize(),
                    placeholder = painterResource(R.drawable.img),
                    error = painterResource(R.drawable.img)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .padding(end = 16.dp),

                    ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (product.originalPrice != null) {
                            Text(
                                text = "${product.originalPrice} k",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                style = TextStyle(lineHeight = 12.sp),
                                modifier = Modifier
                                    .background(Color.Black.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(5.dp))
                        val style = getBadgeStyle(product.tag)
                        Text(
                            text = product.tag.capitalizeFirst(),
                            color = style.textColor,
                            fontSize = 12.sp,
                            style = TextStyle(lineHeight = 12.sp),
                            modifier = Modifier
                                .background(style.backgroundColor, shape = RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    // Title
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = product.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )

                    //Seller & rating
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = product.createdBy.profilePic,
                            contentDescription = "User Avatar",
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.user),
                            error = painterResource(R.drawable.user)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = product.createdBy.firstName, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        if (rating != null) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(text = rating.toString(), fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}