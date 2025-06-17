package com.example.reshare.presentation.features.mainGraph.home.giveAway

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.view.WindowManager
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.reshare.ui.theme.DarkPurple
import com.example.reshare.ui.theme.focusedTextFieldText
import com.example.reshare.ui.theme.textFieldTextContainer
import com.example.reshare.ui.theme.unfocusedTextFieldText

@Composable
fun GiveAwayScreen(
    navController: NavController,
    viewModel: GiveAwayViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val activity = context as? Activity

    // Launcher mở thư viện ảnh
    val imagePickerLauncher
            = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 2)
    ) { uris: List<Uri> -> viewModel.onEvent(GiveAwayUiEvent.AddImages(uris)) }
    val permissionLauncher
            = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted)
            imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        else
            Toast.makeText(context, "Deny photo access", Toast.LENGTH_SHORT).show()
    }
    LaunchedEffect(Unit) {
        @Suppress("DEPRECATION")
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }


    Scaffold(
        topBar = {TopBarCustom(onClick = { navController.popBackStack() })}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            ImagePickerRow(
                selectedImages = state.selectedImages,
                onAddImageClick = {
                    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        Manifest.permission.READ_MEDIA_IMAGES
                    else
                        Manifest.permission.READ_EXTERNAL_STORAGE

                    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                        imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    } else {
                        permissionLauncher.launch(permission)
                    }
                },
                onRemoveImage = { uri ->
                    viewModel.onEvent(GiveAwayUiEvent.RemoveImage(uri))
                }
            )

            ItemForm(
                title = state.title,
                onTitleChange = { viewModel.onEvent(GiveAwayUiEvent.SetTitle(it)) },
                description = state.description,
                onDescriptionChange = {
                    viewModel.onEvent(GiveAwayUiEvent.SetDescription(it))
                },
                pickupTimes = state.pickupTime,
                onPickupTimesChange = {
                    viewModel.onEvent(GiveAwayUiEvent.SetPickupTime(it))
                },
                pickupInstructions = state.instructions,
                onPickupInstructionsChange = {
                    viewModel.onEvent(GiveAwayUiEvent.SetInstructions(it))
                },
                postType = state.postType,
                onPostTypeChange = { viewModel.onEvent(GiveAwayUiEvent.SetPostType(it)) },
                productType = state.productType,
                onProductTypeChange = { viewModel.onEvent(GiveAwayUiEvent.SetProductType(it)) }
            )

            Button(
                onClick = {
                    // viewModel.onEvent(GiveAwayUiEvent.Submit)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 24.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "Create Post",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
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
                text = "Give Away",
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                @Suppress("DEPRECATION")
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        modifier = Modifier.shadow(2.dp)
    )
}

@Composable
fun SelectImage(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.CameraAlt,
            contentDescription = "Upload image",
            tint = Color.Gray
        )
    }
}

@Composable
fun ImagePickerRow(
    selectedImages: List<Uri>,
    onAddImageClick: () -> Unit,
    onRemoveImage: (Uri) -> Unit
) {
    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(26.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedImages.forEach { uri ->
                Box(
                    modifier = Modifier.size(100.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = uri),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = { onRemoveImage(uri) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Remove")
                    }
                }
            }

            // Hiển thị nút chọn ảnh nếu chưa đủ 2 ảnh
            if (selectedImages.size < 2) {
                SelectImage(onClick = onAddImageClick)
            }
        }
    }
}

@Composable
fun ItemForm(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    pickupTimes: String,
    onPickupTimesChange: (String) -> Unit,
    pickupInstructions: String,
    onPickupInstructionsChange: (String) -> Unit,
    postType: String,
    onPostTypeChange: (String) -> Unit,
    productType: String,
    onProductTypeChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFieldText,
                unfocusedContainerColor = MaterialTheme.colorScheme.textFieldTextContainer,
                focusedContainerColor = MaterialTheme.colorScheme.textFieldTextContainer,
            ),
        )

        TextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            placeholder = { Text("e.g. 2 x tins of veg soup, BB Dec 2023") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFieldText,
                unfocusedContainerColor = MaterialTheme.colorScheme.textFieldTextContainer,
                focusedContainerColor = MaterialTheme.colorScheme.textFieldTextContainer,
            ),
        )

        TextField(
            value = pickupTimes,
            onValueChange = onPickupTimesChange,
            label = { Text("Pick up times") },
            placeholder = { Text("e.g. Today from 6pm - 11pm") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFieldText,
                unfocusedContainerColor = MaterialTheme.colorScheme.textFieldTextContainer,
                focusedContainerColor = MaterialTheme.colorScheme.textFieldTextContainer,
            ),
        )

        TextField(
            value = pickupInstructions,
            onValueChange = onPickupInstructionsChange,
            label = { Text("Your pick up instructions") },
            placeholder = {
                Text("e.g. Don’t ring the doorbell, message me when you arrive")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFieldText,
                unfocusedContainerColor = MaterialTheme.colorScheme.textFieldTextContainer,
                focusedContainerColor = MaterialTheme.colorScheme.textFieldTextContainer,
            ),
        )

        Text(text = "Post Type", fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("free", "paid", "wanted").forEach { type ->
                SelectableChip(
                    label = type,
                    isSelected = type == postType,
                    onClick = { onPostTypeChange(type) }
                )
            }
        }

        Text(text = "Product Type", fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("food", "non-food").forEach { type ->
                SelectableChip(
                    label = type,
                    isSelected = type == productType,
                    onClick = { onProductTypeChange(type) }
                )
            }
        }
    }
}

@Composable
fun SelectableChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (isSelected) DarkPurple else Color.LightGray)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}
