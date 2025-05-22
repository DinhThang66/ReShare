package com.example.reshare.presentation.features.mainGraph.community.createPost

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Recycling
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.reshare.R
import com.example.reshare.domain.model.User
import com.example.reshare.presentation.features.mainGraph.community.postDetail.PostDetailViewModel
import com.example.reshare.ui.theme.DarkPurple
import com.example.reshare.ui.theme.LightGray
import com.example.reshare.ui.theme.LightPurple

@Composable
fun CreatePostScreen(
    navController: NavController,
    viewModel: CreatePostViewModel = hiltViewModel()
) {
    var showPhotoDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    // Launcher mở thư viện ảnh
    val imagePickerLauncher
        = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 2)
    ) { uris: List<Uri> ->
        viewModel.onEvent(CreatePostUiEvent.AddImages(uris))
        Toast.makeText(context, "Chọn được 2 ảnh", Toast.LENGTH_SHORT).show()
    }

    val permissionLauncher
        = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted)
            imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        else
            Toast.makeText(context, "Deny photo access", Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(state.postSuccess) {
        if (state.postSuccess) {
            navController.popBackStack()
            Toast.makeText(context, "Posted successfully", Toast.LENGTH_SHORT).show()
            viewModel.onEvent(CreatePostUiEvent.ResetPostSuccess)
        }
    }


    Scaffold(
        topBar = { PostTopBar(navController) },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .navigationBarsPadding()
        ) {
            item {
                PostSection(
                    selectImageClick = {
                        showPhotoDialog = true
                    },
                    selectedImages = state.selectedImages,
                    user = state.user,
                    postContent = state.postContent,
                    updateContent = {
                        viewModel.onEvent(CreatePostUiEvent.UpdateContent(it))
                    }
                )
            }

            item {
                CategoryGrid(
                    categories = categories,
                    selected = state.selectedCategory,
                    onSelected = {
                        viewModel.onEvent(CreatePostUiEvent.SelectCategory(it))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (!state.isSubmitting){
                            viewModel.onEvent(CreatePostUiEvent.SubmitPost)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B0082))
                ) {
                    Text("Submit", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Dialog
        if (showPhotoDialog) {
            ImagePickerDialog(
                onTakePhoto = {
                    showPhotoDialog = false
                    /* TODO */
                },
                onChooseFromLibrary = {
                    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        Manifest.permission.READ_MEDIA_IMAGES
                    else
                        Manifest.permission.READ_EXTERNAL_STORAGE

                    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                        imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    } else {
                        permissionLauncher.launch(permission)
                    }
                    showPhotoDialog = false
                },
                onCancel = { showPhotoDialog = false }
            )
        }
    }

    // Overlay loading khi đang gửi post
    if (state.isSubmitting) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostTopBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = "Create Post",
                fontWeight = FontWeight.SemiBold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(Color.White),
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }
        },
        modifier = Modifier.shadow(2.dp)
    )
}

@Composable
fun PostSection(
    selectImageClick: () -> Unit,
    selectedImages: List<Uri>,
    user: User?,
    postContent: String,
    updateContent: (String) -> Unit
) {
    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            // Avatar + Name
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = user?.profilePic,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.user),
                        error = painterResource(R.drawable.user)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${user?.lastName} ${user?.firstName}",
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable { selectImageClick() },
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            BasicTextField(
                value = postContent,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { updateContent(it) },
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Light
                ),
                minLines = 6,
                maxLines = 6,
                decorationBox = { innerTextField ->
                    if (postContent.isEmpty()) {
                        Text(
                            text = "Share relevant topics with the community",
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Light
                            )
                        )
                    }
                    innerTextField()
                }
            )

            if (selectedImages.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    selectedImages.forEach { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = "Selected image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ImagePickerDialog(
    onTakePhoto: () -> Unit,
    onChooseFromLibrary: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onCancel() },
        title = { Text(text = "Select a Photo", fontWeight = FontWeight.SemiBold) },
        text = {
            Column {
                Text(
                    text = "Take Photo...", fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onTakePhoto() }
                )
                Text(
                    text = "Choose from Library...", fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onChooseFromLibrary() }
                )
                Text(
                    text = "Cancel", fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onCancel() }
                )
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}

data class CategoryItem(val title: String, val iconRes: ImageVector)
val categories = listOf(
    @Suppress("DEPRECATION")
    CategoryItem("App Q&A", Icons.Outlined.HelpOutline),
    CategoryItem("Spreading The Word", Icons.Outlined.Campaign),
    CategoryItem("Zero Waste", Icons.Outlined.Recycling),
    CategoryItem("ReShare Love", Icons.Filled.Favorite),
    CategoryItem("Recipes", Icons.Outlined.RestaurantMenu)
)

@Composable
fun CategoryGrid(
    categories: List<CategoryItem>,
    selected: String?,
    onSelected: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Category", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            categories.chunked(3).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { category ->
                        val isSelected = selected == category.title
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .height(85.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) LightPurple else LightGray)
                                .then(
                                    if (isSelected)
                                        Modifier.border(
                                            width = 2.dp,
                                            color = DarkPurple,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                    else Modifier
                                )
                                .clickable { onSelected(category.title) }
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Icon(
                                imageVector = category.iconRes,
                                contentDescription = category.title,
                                tint = if (isSelected) DarkPurple else Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                category.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color =if (isSelected) DarkPurple else Color.Gray,
                                lineHeight = 14.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                    repeat(3 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}