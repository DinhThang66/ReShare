package com.example.reshare.presentation.features.mainGraph.community.postDetail

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import com.example.reshare.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
        @Suppress("DEPRECATION")
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                modifier = Modifier.shadow(2.dp)
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->

        val combinedInsets = WindowInsets.ime.union(WindowInsets.navigationBars).asPaddingValues()
        var comment by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                PostHeader(
                    avatarRes = R.drawable.img,
                    username = "Joi",
                    category = "Zero Waste",
                    timeAgo = "6 hours ago",
                    isNewbie = true,
                    content = "Instead of buying expensive chocolate and ending up either lots of packaging. I have collected scraps to make Easter gifts. The little skirt is reversible.",
                    commentsCount = 6,
                    likesCount = 28,
                    postImageRes = R.drawable.img // Thêm ảnh cho post
                )

                CommentItem(username = "EF", comment = "That skirt is adorable, I'd wear that print and I'm a kicking on for 50! So talented, and such a good eye for style. Well done Joi!", likes = 0, time = "Just now")
                CommentItem(username = "Craigy", comment = "Helloooooooooo Edward 😊😊", likes = 2, time = "10 hours ago")
                CommentItem(username = "Trace", comment = "Evening Edward 🌅", likes = 1, time = "10 hours ago")
                CommentItem(username = "Dharma", comment = "Hello @Edward welcome to ReShare xx", likes = 2, time = "9 hours ago")


                Spacer(modifier = Modifier.height(16.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(combinedInsets)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    placeholder = { Text("Leave your comment here...") },
                    trailingIcon = {
                        TextButton(onClick = { /* Post comment logic */ }) {
                            Text("Post")
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
    avatarRes: Int,
    username: String,
    category: String,
    timeAgo: String,
    isNewbie: Boolean,
    content: String,
    commentsCount: Int,
    likesCount: Int,
    postImageRes: Int? = null
) {
    Row(
        modifier = Modifier.padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
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

    postImageRes?.let { res ->
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = res),
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("$likesCount likes", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        }
    }
}


@Composable
fun CommentItem(username: String, comment: String, likes: Int, time: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(R.drawable.img),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
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