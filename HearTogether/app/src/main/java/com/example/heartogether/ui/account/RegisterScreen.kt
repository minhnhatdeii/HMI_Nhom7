package com.example.heartogether.ui.account

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
//import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
//import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartogether.R
//import com.example.heartogether.presentation.viewmodel.AuthState
//import com.example.heartogether.presentation.viewmodel.AuthenticationViewModel
import com.example.heartogether.ui.theme.NotoSans

private val gradientBackground =Color(0xFF9AD983)


@Composable
fun RegisterScreen(
    onRegisterButtonClicked: () -> Unit = {},
    onLoginButtonClicked: () -> Unit = {},
//    authenticationViewModel: AuthenticationViewModel = hiltViewModel()
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var username by remember {
        mutableStateOf("")
    }

    var isPasswordVisible by remember { mutableStateOf(false) }

    var confirmPassword by remember {
        mutableStateOf("")
    }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    var showVerificationDialog by remember { mutableStateOf(false) }

//    val authState = authenticationViewModel.authState.observeAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

//    LaunchedEffect(authState.value) {
//        when (authState.value) {
//            is AuthState.EmailNotVerified -> {
//                showVerificationDialog = true
//            }
//
//            is AuthState.Error -> {
//                val errorMessage = (authState.value as AuthState.Error).message
//                // Show error message to the
//                // For example, you can use a Toast or a Snackbar
//                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//            }
//
//            else -> {}
//        }
//    }

    if (showVerificationDialog) {
        AlertDialog(
            onDismissRequest = { showVerificationDialog = false },
            title = {
                Text(
                    "Xác thực tài khoản",
                    fontFamily = NotoSans,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Xác thực Gmail của bạn trước khi đăng nhập.",
                    fontFamily = NotoSans,
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showVerificationDialog = false
                        onRegisterButtonClicked() // Điều hướng tới trang đăng nhập
                    }
                ) {
                    Text(
                        "OK",
                        fontFamily = NotoSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF00FAF2)
                    )
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .clickable { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.heartogether), // Replace with your logo icon
            contentDescription = "Logo",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Title
        Text(
            text = "Registration",
            fontFamily = NotoSans,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Email Input Field
        OutlinedTextField(
            value = email,
            textStyle = TextStyle(fontFamily = NotoSans, fontSize = 16.sp),
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Email ID", color = Color.Gray, fontFamily = NotoSans) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00FAF2),
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Username input field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Username", color = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00FAF2),
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Input Field
        OutlinedTextField(
            value = password,
            textStyle = TextStyle(fontFamily = NotoSans, fontSize = 16.sp),
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Password", color = Color.Gray, fontFamily = NotoSans) },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = if (isPasswordVisible) R.drawable.icons8_hide_60 else R.drawable.icons8_eye_60),
                    contentDescription = "Toggle Password Visibility",
                    tint = Color.Gray,
                    modifier = Modifier.clickable {
                        isPasswordVisible = !isPasswordVisible
                    }
                )
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00FAF2),
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Input Field
        OutlinedTextField(
            value = confirmPassword,
            textStyle = TextStyle(fontFamily = NotoSans, fontSize = 16.sp),
            onValueChange = { confirmPassword = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Confirm Password", color = Color.Gray, fontFamily = NotoSans) },
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = if (isConfirmPasswordVisible) R.drawable.icons8_hide_60 else R.drawable.icons8_eye_60),
                    contentDescription = "Toggle Password Visibility",
                    tint = Color.Gray,
                    modifier = Modifier.clickable {
                        isConfirmPasswordVisible = !isConfirmPasswordVisible
                    }
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00FAF2),
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Register Button
        Button(
            onClick = {
                if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                } else {
//                    authenticationViewModel.signup(email, password, username)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(gradientBackground, shape = MaterialTheme.shapes.medium),
            contentPadding = PaddingValues(0.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradientBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Register",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = NotoSans,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text(
                text = "Bạn đã có tài khoản? ",
                color = Color.Gray,
                fontFamily = NotoSans,
                fontSize = 14.sp
            )
            Text(
                text = "Đăng nhập",
                color = Color(0xFF00FAF2),
                fontFamily = NotoSans,
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    onLoginButtonClicked()
                }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}
