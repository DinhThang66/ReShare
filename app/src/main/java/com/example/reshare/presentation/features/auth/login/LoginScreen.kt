package com.example.reshare.presentation.features.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.reshare.R
import com.example.reshare.presentation.components.TextFieldCustom
import com.example.reshare.presentation.utils.Screen
import com.example.reshare.ui.theme.BlueGray

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.state.collectAsState()

    Surface{
        Column(modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
        ) {
            TopSection()
            Spacer(modifier = Modifier.height(36.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 30.dp)
            ) {
                LoginSection(
                    email = state.email,
                    password = state.password,
                    onEmailChange = { viewModel.onEvent(LoginUiEvent.OnEmailChange(it)) },
                    onPasswordChange = { viewModel.onEvent(LoginUiEvent.OnPasswordChange(it)) },
                    onLogin = { viewModel.onEvent(LoginUiEvent.Submit(state.email, state.password)) }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(10.dp).align(Alignment.Center)
                        )
                    }
                    if (state.error.isNotBlank()) {
                        Text("Error: ${state.error}", color = Color.Red, fontSize = 12.sp)
                    }
                }

                CreateAccountText(onCreateNowClick = {
                    navController.navigate(Screen.Register.route)
                })
            }
        }
    }
}

@Composable
fun TopSection() {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(R.drawable.shape),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.46f),
            contentScale = ContentScale.FillBounds
        )

        Row(
            modifier = Modifier.padding(top = 80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                modifier = Modifier.size(42.dp),
                tint = uiColor
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(
                    text = "ReShare",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = uiColor
                )
                Text(
                    text = "Share more, Waste less",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = uiColor
                )
            }
        }
        Text(
            text = "Login",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp,
            color = uiColor,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(alignment = Alignment.BottomCenter)
        )
    }
}

@Composable
fun LoginSection(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit
) {
    TextFieldCustom(
        label = "Email",
        value = email,
        onValueChange = onEmailChange,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(15.dp))
    TextFieldCustom(
        label = "Password",
        value = password,
        onValueChange = onPasswordChange,
        modifier = Modifier.fillMaxWidth(),
        isPassword = true
    )
    Spacer(modifier = Modifier.height(20.dp))
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        onClick = { onLogin() },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSystemInDarkTheme()) BlueGray else Color.Black,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = "Login",
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

@Composable
fun CreateAccountText(
    onCreateNowClick: () -> Unit
) {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    Box(
        modifier = Modifier
            .fillMaxHeight(fraction = 0.8f)
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Row {
            Text(
                text = "Don't have account?",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Create now",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = uiColor,
                modifier = Modifier.clickable { onCreateNowClick() }
            )
        }
    }
}