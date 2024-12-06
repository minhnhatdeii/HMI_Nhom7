package com.example.heartogether.ui.screen.signdictionary

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heartogether.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignDictionaryScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dictionary",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    ) { paddingValues ->
        val searchText = remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Search bar
            TextField(
                value = searchText.value,
                onValueChange = { searchText.value = it }, // Cập nhật nội dung nhập
                placeholder = { Text("Type sign you want") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    containerColor = Color.Gray.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(8.dp) // Bo góc TextField
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = "Sign for Hello",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            // Image
            Image(
                painter = painterResource(id = R.drawable.hello_flash_card_), // Thay bằng hình ảnh thực tế
                contentDescription = "Sign for Hello",
                modifier = Modifier
                    .size(400.dp) // Tăng kích thước ảnh
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Hint
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = "Hint")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Hello hint: Like a salute",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
fun SignDictionaryScreenPreview() {
    SignDictionaryScreen()
}
