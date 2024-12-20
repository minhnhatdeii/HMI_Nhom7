package com.example.heartogether.ui.account


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
//import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heartogether.R
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.example.harmonyhub.R
//import com.example.harmonyhub.presentation.viewmodel.AuthState
//import com.example.harmonyhub.presentation.viewmodel.AuthenticationViewModel
import com.example.heartogether.ui.theme.NotoSans


private val gradientBackground =Color(0xFF9AD983)



@Composable
fun LoginScreen(
    onLoginButtonClicked: () -> Unit = {},
    onRegisterButtonClicked: () -> Unit = {},
    //authenticationViewModel: AuthenticationViewModel = hiltViewModel(),
    onForgotPasswordButtonClicked: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) } // Trạng thái hiển thị mật khẩu
    val focusManager = LocalFocusManager.current

    //val authState = authenticationViewModel.authState.observeAsState()
    val context = LocalContext.current

//    LaunchedEffect(authState.value) {
//        when (authState.value) {
//            is AuthState.Authenticated -> {
//                onLoginButtonClicked()
//            }
//            is AuthState.Error -> {
//                val errorMessage = (authState.value as AuthState.Error).message
//                // Show error message to the user
//                // For example, you can use a Toast or a Snackbar
//                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//            }
//            else -> { }
//        }
//    }
    //Spacer(Modifier.height(300.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .clickable { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.img_7), // Replace with your logo icon
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
                .clip(shape = RoundedCornerShape(16.dp))
            //.fillMaxWidth() ,
            // contentScale = ContentScale.Crop,

        )
        Image(
            painter = painterResource(id = R.drawable.img_9), // Replace with your logo icon
            contentDescription = "Logo",
            modifier = Modifier.width(300.dp)
                .fillMaxWidth()
                .height(150.dp)
                .padding(start = 10.dp),
            // contentScale = ContentScale.Crop,

        )

        // Email Input Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            textStyle = TextStyle(fontFamily = NotoSans, fontSize = 16.sp),
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Account", color = Color.Gray, fontFamily = NotoSans) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icons8_google_48), // Replace with your email icon
                    contentDescription = "Email Icon",
                    tint = Color.Gray
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF9AD983),
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.White
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Input Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            textStyle = TextStyle(fontFamily = NotoSans, fontSize = 16.sp),
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Password", color = Color.Gray, fontFamily = NotoSans) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icons8_google_48), // Replace with your password icon
                    contentDescription = "Password Icon",
                    tint = Color.Gray
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = if (passwordVisible) R.drawable.icons8_hide_60 else R.drawable.icons8_eye_60), // Thay đổi biểu tượng mắt
                    contentDescription = "Toggle Password Visibility",
                    tint = Color.Gray,
                    modifier = Modifier.clickable {
                        passwordVisible = !passwordVisible // Chuyển đổi trạng thái hiển thị mật khẩu
                    }
                )
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(), // Thay đổi cách hiển thị mật khẩu
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF9AD983),
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.White
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Remember Me Checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it },
                colors = CheckboxDefaults.colors(
                    checkmarkColor = Color.White,
                    uncheckedColor = Color.Gray,
                    checkedColor = Color(0xFF9AD983)
                )
            )
            Text(
                text = "Remember me",
                fontFamily = NotoSans,
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Login Button
        Button(
            onClick = {
                onLoginButtonClicked()
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
            Text(
                text = "Log in",
                fontFamily = NotoSans,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Forgot Password
        Text(
            text = AnnotatedString("Forgot the password?"),
            fontFamily = NotoSans,
            fontSize = 14.sp,
            color = Color(0xFF9AD983),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { onForgotPasswordButtonClicked() },
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Sign Up
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
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}

