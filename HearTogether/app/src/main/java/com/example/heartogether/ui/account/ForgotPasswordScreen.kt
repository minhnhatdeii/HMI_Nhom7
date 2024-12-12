package com.example.heartogether.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.example.heartogether.presentation.viewmodel.AuthenticationViewModel
import com.example.heartogether.ui.theme.NotoSans

private val gradientBackground =Color(0xFF9AD983)


@Composable
fun ForgotPasswordScreen(
    onBackButtonClicked: () -> Unit,
    onVerifyButtonClicked: () -> Unit,
    onRegisterButtonClicked: () -> Unit,
//    authenticationViewModel: AuthenticationViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var isValidEmail by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clickable { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Thanh tiêu đề
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { onBackButtonClicked() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Forgot the password",
                style = TextStyle(
                    fontFamily = NotoSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black
                ),
            )
        }

        Spacer(modifier = Modifier.height(100.dp))

        Text(
            text = "Your email",
            style = TextStyle(
                fontFamily = NotoSans,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isValidEmail = isValidEmail(email)
            },
            textStyle = TextStyle(
                fontFamily = NotoSans,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            placeholder = { Text("example@gmail.com", color = Color.Gray) },
            isError = !isValidEmail,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isValidEmail) Color(0xFF9AD983) else Color.Red,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = {
                if (isValidEmail(email)) {
//                    authenticationViewModel.resetPassword(email)
                    showDialog = true
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(50.dp)
                .background(gradientBackground, shape = MaterialTheme.shapes.medium),
            contentPadding = PaddingValues(0.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "Sent code", color = Color.Black, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Don’t have an account? ",
                fontFamily = NotoSans,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = AnnotatedString("Sign Up"),
                fontFamily = NotoSans,
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    onRegisterButtonClicked()
                },
                color = Color(0xFF9AD983)
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text("Notification", fontWeight = FontWeight.Bold, fontFamily = NotoSans)
            },
            text = {
                Text(
                    "Check your email to reset your password.",
                    fontFamily = NotoSans,
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        onVerifyButtonClicked() // Chuyển hướng đến trang đăng nhập
                    }
                ) {
                    Text("OK",
                        fontFamily = NotoSans,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9AD983))
                }
            }
        )
    }
}

// Hàm kiểm tra định dạng email
fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
    return emailRegex.matches(email)
}

@Preview
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(
        onBackButtonClicked = {},
        onVerifyButtonClicked = {},
        onRegisterButtonClicked = {}
    )
}
