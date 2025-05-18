package com.example.reshare.presentation.features.mainGraph.community

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.reshare.domain.model.Post
import com.example.reshare.presentation.components.CommunityPostCard
import com.example.reshare.presentation.utils.Screen
import com.example.reshare.presentation.utils.categories
import com.example.reshare.ui.theme.DarkPurple
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun CommunityScreen(
    innerPadding: PaddingValues,
    navController: NavHostController,
    viewModel: CommunityViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val updatedPost = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.get<Post>("updatedPost")

    val listState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showSheet by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isInitialLoading,
        onRefresh = { viewModel.onEvent(CommunityUiEvent.Refresh) }
    )

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.collect { lastVisibleItemIndex ->
            if (
                lastVisibleItemIndex == state.posts.size - 1 &&
                !state.isPaginating &&
                !state.isLastPage
                ) {
                viewModel.onEvent(CommunityUiEvent.LoadNextPage)
            }
        }
    }

    // Bắt sự kiện thay đổi trạng thái từ PostDetail về CommunityScreen
    LaunchedEffect(updatedPost) {
        updatedPost?.let { post ->
            viewModel.onEvent(CommunityUiEvent.UpdatePost(post))
            navController.currentBackStackEntry?.savedStateHandle?.remove<Post>("updatedPost")
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
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Posts",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Button(
                    onClick = { showSheet = true },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkPurple),
                    border = BorderStroke(1.dp, DarkPurple)
                ) {
                    Text(text = "Category")
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Posts
            when {
                state.isInitialLoading  -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        item {
                            Text(text = "Lỗi: ${state.error}")
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize() // Or .weight(1f)
                            .background(Color.White),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(state.posts) { post -> CommunityPostCard(
                            username = "${post.createdBy.lastName} ${post.createdBy.firstName}",
                            avatar = post.createdBy.profilePic,
                            category = "Spreading The Word",
                            timeAgo = post.createdAt,
                            content = post.content,
                            commentsCount = post.commentsCount,
                            likesCount = post.likesCount,
                            isLiked = post.likedByCurrentUser,
                            onLikeClick = {
                                viewModel.onEvent(CommunityUiEvent.ToggleLike(post.id))
                            },
                            imageUrls = post.images,
                            onClick = {
                                //navController.navigate(Screen.PostDetail.route)
                                navController.currentBackStackEntry?.savedStateHandle?.set("post", post)
                                navController.navigate(Screen.PostDetail.route)
                            }
                        )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        if (state.isPaginating && !state.isLastPage) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Sheet
            if (showSheet) {
                LaunchedEffect(Unit) {
                    sheetState.expand()
                }

                ModalBottomSheet(
                    onDismissRequest = { showSheet = false },
                    sheetState = sheetState,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .navigationBarsPadding()
                    ) {
                        Text(
                            text = "Category",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            categories.forEach { category ->
                                val isSelected = selectedCategory == category
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) Color(0xFFF1E7FF)
                                            else Color.White
                                        )
                                        .clickable { selectedCategory = category }
                                        .padding(horizontal = 16.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (isSelected) DarkPurple else Color.LightGray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = category,
                                        fontSize = 13.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                showSheet = false
                                // Apply selectedCategory here
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkPurple)
                        ) {
                            Text(text = "Apply", color = Color.White)
                        }
                    }
                }
            }
        }


        // Pull-to-refresh indicator
        PullRefreshIndicator(
            refreshing = state.isInitialLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}