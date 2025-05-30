package com.example.reshare.presentation.features.auth.register

import android.app.Activity
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.reshare.presentation.features.auth.login.LoginUiEvent
import com.example.reshare.ui.theme.BlueGray
import com.example.reshare.ui.theme.DarkBlue

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current
    val activity = context as? Activity
    LaunchedEffect(Unit) {
        @Suppress("DEPRECATION")
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            Toast.makeText(context, "Registered successfully!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = { CustomTopAppBar(onBackClick = {navController.popBackStack() } ) },
    ) { paddingValues ->
        val combinedInsets = WindowInsets.ime.union(WindowInsets.navigationBars).asPaddingValues()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .background(
                    color = if (isSystemInDarkTheme()) Color.Black else Color.White
                )
                .padding(combinedInsets),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
        ) {
            item{
                RegisterFormFields(
                    firstName = state.firstName, firstNameError = state.firstNameError,
                    lastName = state.lastName, lastNameError = state.lastNameError,
                    email = state.email, emailError = state.emailError,
                    password = state.password, passwordError = state.passwordError,
                    confirmPassword = state.confirmPassword,
                    confirmPasswordError = state.confirmPasswordError,
                    onFirstNameChange = { viewModel.onEvent(RegisterUiEvent.OnFirstNameChange(it)) },
                    onLastNameChange = { viewModel.onEvent(RegisterUiEvent.OnLastNameChange(it)) },
                    onEmailNameChange = { viewModel.onEvent(RegisterUiEvent.OnEmailNameChange(it)) },
                    onPasswordChange = { viewModel.onEvent(RegisterUiEvent.OnPasswordChange(it)) },
                    onConfirmPasswordChange = { viewModel.onEvent(RegisterUiEvent.OnConfirmPasswordChange(it)) }
                )

                BtnRegisterAndShowState(
                    onBtnClick = {
                        viewModel.onEvent(RegisterUiEvent.ValidateForm(
                            state.firstName, state.lastName, state.email,
                            state.password, state.confirmPassword
                        ))
                        if (state.isValid) {
                            viewModel.onEvent(RegisterUiEvent.Submit(
                                state.firstName, state.lastName, state.email, state.password
                            ))
                        }
                    },
                    isLoading = state.isLoading,
                    error = state.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Join ReShare",
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )},
        colors = TopAppBarDefaults.topAppBarColors(
            if (isSystemInDarkTheme()) DarkBlue else Color.White
        ),
        navigationIcon = {
            IconButton(onBackClick) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBackIosNew,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        modifier = Modifier.shadow(2.dp)
    )
}

@Composable
fun RegisterFormFields(
    firstName: String, firstNameError: String,
    lastName: String, lastNameError: String,
    email: String, emailError: String,
    password: String, passwordError: String,
    confirmPassword: String, confirmPasswordError: String,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
) {
    TextFieldCustom(
        label = "First name (display name)",
        value = firstName,
        onValueChange = onFirstNameChange,
        errorMessage = firstNameError,
        capitalization = KeyboardCapitalization.Words
    )

    TextFieldCustom(
        label = "Last name",
        value = lastName,
        onValueChange = onLastNameChange,
        errorMessage = lastNameError,
        capitalization = KeyboardCapitalization.Words
    )

    TextFieldCustom(
        label = "Email",
        value = email,
        onValueChange = onEmailNameChange,
        errorMessage = emailError
    )

    TextFieldCustom(
        label = "Password",
        value = password,
        onValueChange = onPasswordChange,
        isPassword = true,
        errorMessage = passwordError
    )

    TextFieldCustom(
        label = "Confirm password",
        value = confirmPassword,
        onValueChange = onConfirmPasswordChange,
        isPassword = true,
        errorMessage = confirmPasswordError
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
//            .clickable { agreePolicy = !agreePolicy }
//            .padding(vertical = 8.dp)
    ) {
        //Checkbox(checked = agreePolicy, onCheckedChange = { agreePolicy = it })
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = "I agree to the terms and privacy policy")
    }
}

@Composable
fun BtnRegisterAndShowState(
    onBtnClick: () -> Unit,
    isLoading: Boolean,
    error: String
) {
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        onClick = onBtnClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSystemInDarkTheme()) BlueGray else Color.Black,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = "Join Now",
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

    // Loading spinner
    if (isLoading) {
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    // Error message
    if (error.isNotBlank()) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = error,
            color = Color.Red,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun TextFieldCustom(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    errorMessage: String? = null,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None
)   {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp) // điều chỉnh
                .drawBehind {
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                },
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black,
                    lineHeight = 20.sp,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                visualTransformation = if (isPassword && !passwordVisible)
                    PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = capitalization
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, bottom = 4.dp),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        innerTextField()
                        if (isPassword) {
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = if (passwordVisible)
                                    Icons.Default.VisibilityOff
                                else Icons.Default.Visibility,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clickable { passwordVisible = !passwordVisible }
                            )
                        }
                    }
                }
            )
        }
    }
    if (!errorMessage.isNullOrBlank()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Color(0xFFB00020),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = errorMessage,
                color = Color(0xFFB00020),
                fontSize = 13.sp
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}