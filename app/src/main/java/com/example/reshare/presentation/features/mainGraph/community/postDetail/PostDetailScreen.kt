package com.example.reshare.presentation.features.mainGraph.community.postDetail

import android.app.Activity
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.reshare.R
import com.example.reshare.domain.model.Post
import com.example.reshare.presentation.features.mainGraph.community.CommunityUiEvent
import com.example.reshare.presentation.utils.Resource
import com.example.reshare.presentation.utils.Screen
import com.example.reshare.presentation.utils.formatTimeAgo
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    navController: NavController,
    viewModel: PostDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity
    val listState = rememberLazyListState()
    var commentText by remember { mutableStateOf("") }

    val post = state.post
    val highlightedId = state.highlightedCommentId

    // Set post and load comments
    LaunchedEffect(Unit) {
        @Suppress("DEPRECATION")
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val savedPost = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Post>("post")
        savedPost?.let {
            viewModel.onEvent(PostDetailUiEvent.SetPost(it))
            viewModel.onEvent(PostDetailUiEvent.LoadComments(it.id))
        }
    }

    // Handle comment post result
    LaunchedEffect(state.isAddingComment, state.highlightedCommentId, state.addCommentError) {
        if (!state.isAddingComment && state.addCommentError == null && state.highlightedCommentId != null) {
            commentText = ""
            listState.animateScrollToItem(state.comments.lastIndex)
            viewModel.onEvent(PostDetailUiEvent.ResetAddCommentState)
        }

        if (state.addCommentError != null) {
            Toast.makeText(context, state.addCommentError, Toast.LENGTH_SHORT).show()
            viewModel.onEvent(PostDetailUiEvent.ResetAddCommentState)
        }
    }
    
    // Khi thoát màn hình detail thì trả về trạng thái đã thay đổi về màn hình Community
    BackHandler {
        viewModel.state.value.post?.let { updatedPost ->
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("updatedPost", updatedPost)
        }
        navController.popBackStack()
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.state.value.post?.let { updatedPost ->
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("updatedPost", updatedPost)
                            }
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color.White),
                modifier = Modifier.shadow(2.dp)
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        val combinedInsets = WindowInsets.ime.union(WindowInsets.navigationBars).asPaddingValues()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    post?.let {
                        PostHeader(
                            avatar = it.createdBy.profilePic,
                            username = "${it.createdBy.lastName} ${it.createdBy.firstName}",
                            category = "Zero Waste",
                            timeAgo = formatTimeAgo(it.createdAt),
                            isNewbie = true,
                            content = it.content,
                            commentsCount = it.commentsCount,
                            likesCount = it.likesCount,
                            postImage = it.images.firstOrNull(),
                            isLiked = it.likedByCurrentUser,
                            onLikeClick = {
                                viewModel.onEvent(PostDetailUiEvent.ToggleLike(post.id))
                            },
                            onAvatarClick = {
                                navController.navigate(Screen.UserProfile.route + "/${it.createdBy.id}")
                            }
                        )
                    }
                }

                // Comment List
                when {
                    state.isLoading -> item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    state.error != null -> item {
                        Text("Lỗi: ${state.error}", color = Color.Red)
                    }

                    else -> {
                        items(state.comments) { comment ->
                            CommentItem(
                                username = "${comment.createdBy.lastName} ${comment.createdBy.firstName}",
                                avatar = comment.createdBy.profilePic,
                                comment = comment.content,
                                likes = comment.likes.size,
                                time = formatTimeAgo(comment.createdAt),
                                highlight = comment.id == highlightedId,
                                onAvatarClick = {
                                    navController.navigate(Screen.UserProfile.route + "/${comment.createdBy.id}")
                                }
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            // Comment input
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(combinedInsets)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = { Text("Leave your comment here...") },
                    trailingIcon = {
                        val isPosting = state.isAddingComment

                        TextButton(onClick = {
                            if (commentText.isNotBlank() && !isPosting && post != null) {
                                viewModel.onEvent(PostDetailUiEvent.AddComment(commentText, post.id))
                            }
                        }) {
                            if (isPosting) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            } else {
                                Text("Post")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun PostHeader(
    avatar: String,
    username: String,
    category: String,
    timeAgo: String,
    isNewbie: Boolean,
    content: String,
    commentsCount: Int,
    likesCount: Int,
    isLiked: Boolean = false,
    onLikeClick: () -> Unit = {},
    postImage: String? = null,
    onAvatarClick: () -> Unit = {}
) {
    // LIKE with press effect
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction: Interaction ->
            isPressed = interaction is PressInteraction.Press
        }
    }

    Row(
        modifier = Modifier.padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = avatar,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable(onClick = onAvatarClick),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.user),
            error = painterResource(R.drawable.user)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "$username posted in $category",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.Gray
                )
                Spacer(Modifier.width(4.dp))
                Text(timeAgo, fontSize = 12.sp, color = Color.Gray)
                if (isNewbie) {
                    Spacer(Modifier.width(6.dp))
                    Text("🐣 Newbie", fontSize = 12.sp)
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Text(text = content)

    postImage?.let { res ->
        Spacer(modifier = Modifier.height(8.dp))
        AsyncImage(
            model = res,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.3f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("$commentsCount comments", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        }
        Spacer(modifier = Modifier.width(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(
                onClick = onLikeClick,
                indication = null,          // Tắt ripple
                interactionSource = interactionSource
            )
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                tint = if (isLiked)
                    Color.Red.copy(alpha = if (isPressed) 0.6f else 1f)
                else
                    Color.Gray.copy(alpha = if (isPressed) 0.6f else 1f),
                contentDescription = null, modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "$likesCount likes",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray.copy(alpha = if (isPressed) 0.6f else 1f)
            )
        }
    }
}


@Composable
fun CommentItem(
    username: String, avatar: String, comment: String, likes: Int, time: String,
    highlight: Boolean,
    onAvatarClick: () -> Unit = {}
) {
    var showHighlight by remember { mutableStateOf(highlight) }
    // Tự động tắt highlight sau 3 giây
    LaunchedEffect(highlight) {
        if (highlight) {
            delay(3000)
            showHighlight = false
        }
    }

    val bgColor by animateColorAsState(
        if (showHighlight) Color(0xFFE0F7FA) else Color.Transparent,
        label = "highlight"
    )

    Column(modifier = Modifier.padding(vertical = 8.dp).background(bgColor)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = avatar,
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable { onAvatarClick() },
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.user),
                error = painterResource(R.drawable.user)

            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(username, fontWeight = FontWeight.SemiBold)
                Text(comment)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(time, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = "Likes",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(likes.toString(), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }
    }

    HorizontalDivider()
}