package com.example.reshare.presentation.features.mainGraph.home.makeARequest

import android.app.Activity
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.reshare.domain.model.Product
import com.example.reshare.ui.theme.DarkPurple
import com.example.reshare.ui.theme.Pink

@Composable
fun MakeRequestScreen(
    navController: NavController,
    viewModel: MakeRequestViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current
    val activity = context as? Activity
    LaunchedEffect(Unit) {
        @Suppress("DEPRECATION")
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }
    // Set product
    LaunchedEffect(Unit) {
        val savedProduct = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Product>("product")
        savedProduct?.let {
            viewModel.onEvent(MakeRequestUiEvent.SetProduct(it))
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.remove<Product>("product")
        }
    }
    LaunchedEffect(state.success, state.error) {
        if (state.success) {
            Toast.makeText(context, "Request sent successfully", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
        if (state.error.isNotBlank()) {
            Toast.makeText(context, "Error: ${state.error}", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = { TopBarCustom(onClick = { navController.popBackStack() }) },
        bottomBar = { BottomBarCustom(onClick = { state.product?.let{
            viewModel.onEvent(MakeRequestUiEvent.SubmitRequest(it.id)) }},
            isLoading = state.isLoading
        )}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            state.product?.let{ product ->
                PostHeaderInfo(
                    avatar = product.createdBy.profilePic,
                    rating = "5.0",
                    giverName = product.createdBy.firstName,
                    pickupTimes = product.pickupTimes
                )
                Spacer(modifier = Modifier.height(16.dp))

                PickupTimeInputField(
                    value = state.pickupTime,
                    onValueChange = { viewModel.onEvent(MakeRequestUiEvent.PickupTimeChanged(it)) },
                    isError = state.error.contains("Pickup time", ignoreCase = true),
                )
                Spacer(modifier = Modifier.height(16.dp))

                OptionalMessageInputField(
                    value = state.message,
                    onValueChange = {
                        viewModel.onEvent(MakeRequestUiEvent.MessageChanged(it))
                    }
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
                text = "Make a Request",
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                @Suppress("DEPRECATION")
                (Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back"
                ))
            }
        },
        modifier = Modifier.shadow(2.dp)
    )
}

@Composable
fun BottomBarCustom(
    onClick: () -> Unit,
    isLoading: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkPurple,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "Send Request",
                fontWeight = FontWeight.SemiBold
            )
            if (isLoading) {
                Spacer(modifier = Modifier.width(8.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
            }
        }
    }
}

@Composable
fun PostHeaderInfo(
    avatar: String,
    rating: String,
    giverName: String,
    pickupTimes: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier
            .size(48.dp)
            .offset(y = (-10).dp)) {
            AsyncImage(
                model = avatar,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.user),
                error = painterResource(R.drawable.user)
            )

            // Rating badge
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 6.dp)
                    .background(Pink, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = Color.White
                )
                Text(
                    text = rating,
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                Text(
                    text = "$giverName's pick up time:",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = "\"$pickupTimes\"",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun PickupTimeInputField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    maxLength: Int = 50
) {
    val remaining = maxLength - value.length

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Please specify a pickup time & date",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = value,
            onValueChange = {
                if (it.length <= maxLength) onValueChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            isError = isError,
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            placeholder = { Text(text = "Time you can collect") },
            supportingText = {
                if (isError) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Pickup time field cannot be blank",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Required",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "$remaining/$maxLength",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun OptionalMessageInputField(
    value: String,
    onValueChange: (String) -> Unit,
    maxLength: Int = 200
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Add a message to your request",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = value,
            onValueChange = {
                if (it.length <= maxLength) onValueChange(it)
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text("Send a friendly message to Shaz") },
            singleLine = false,
            maxLines = 3,
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Optional",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}


/*
@Composable
fun ProductInfoRow(
    imageRes: Int,
    title: String,
    storeName: String,
    storeLabelColor: Color = Color.Red
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
                color = Color.Black,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = storeName.uppercase(),
                    color = storeLabelColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "From $storeName",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }

    HorizontalDivider()
}
 */

