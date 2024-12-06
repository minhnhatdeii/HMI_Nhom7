package com.example.heartogether.profile

import androidx.compose.foundation.Image
import androidx.compose.material3.TextButton
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
//import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartogether.R
//import com.example.heartogether.presentation.viewmodel.UserDataViewModel
import com.example.heartogether.ui.theme.NotoSans

@Composable
fun ProfileScreen(
    onBackButtonClicked: () -> Unit,
//    userDataViewModel: UserDataViewModel = hiltViewModel(),
) {
    val focusManager = LocalFocusManager.current

    val profileImage = remember { mutableIntStateOf(R.drawable.hello_flash_card_) }
//    val username = userDataViewModel.userName.observeAsState()
//    val email = userDataViewModel.email.observeAsState()

    val isImageDialogOpen = remember { mutableStateOf(false) }

    if (isImageDialogOpen.value) {
        AlertDialog(
            onDismissRequest = { isImageDialogOpen.value = false },
            title = { Text("Thay đổi ảnh đại diện") },
            text = {
                Text("Chọn ảnh từ thư viện hoặc máy của bạn.")
            },
            confirmButton = {
                TextButton(
                    onClick = { isImageDialogOpen.value = false },
                ) {
                    Text(
                        text = "Lưu",
                        fontFamily = NotoSans,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00FAF2)
                    )

                }
            },
            dismissButton = {
                TextButton(
                    onClick = { isImageDialogOpen.value = false },
                ) {
                    Text(
                        text = "Hủy",
                        fontFamily = NotoSans,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00FAF2)
                    )
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    isImageDialogOpen.value = false
                    focusManager.clearFocus()
                })
            }
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Thanh tiêu đề
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
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
                text = "Thông tin cá nhân",
                style = TextStyle(
                    fontFamily = NotoSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.White
                )
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Khi nhấn vào ảnh, mở hộp thoại thay đổi ảnh
                Image(
                    painter = painterResource(id = profileImage.intValue),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                isImageDialogOpen.value = true
                            })
                        }
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Khi nhấn vào tên, mở hộp thoại thay đổi tên
                Text(
                    text = "Dat",//username.value.toString(),
                    style = TextStyle(
                        fontFamily = NotoSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                    ),
                    modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures(onTap = {
                        })
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Email",
                style = TextStyle(
                    fontFamily = NotoSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Dat@gmail.com",//email.value.toString(),
                style = TextStyle(
                    fontFamily = NotoSans,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bạn bè",
                style = TextStyle(
                    fontFamily = NotoSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            val numberOfFriends = 10
            Text(
                text = "$numberOfFriends bạn bè",
                style = TextStyle(
                    fontFamily = NotoSans,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Nút bấm để chuyển hướng đến trang danh sách bạn bè
            TextButton(
                onClick = {  },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Xem tất cả bạn bè",
                    fontFamily = NotoSans,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00FAF2)
                )
            }

        }
    }
}




