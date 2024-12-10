package com.example.heartogether.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.heartogether.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonsScreen(
    onBackButtonClicked: () -> Unit,
    onOptionSelected: (String) -> Unit // Callback khi chọn câu trả lời
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Training Sign Language") },
                navigationIcon = {
                    IconButton(onClick = onBackButtonClicked) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Notifications */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications"
                        )
                    }
                }
            )
        },
        bottomBar = {
            HintBar(hintText = "Hint: like squeezing an orange under chin")
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hình ảnh chính ở giữa
            Image(
                painter = painterResource(id = R.drawable.image_28), // Thay bằng hình ảnh chính của bạn
                contentDescription = "Main Training Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(16.dp)
            )

            Text(
                text = "Which one is orange?",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OptionItem(
                    imageRes = R.drawable.image_28,
                    text = "Coffee",
                    onClick = { onOptionSelected("Coffee") }
                )
                OptionItem(
                    imageRes = R.drawable.ic_launcher_background,
                    text = "Orange",
                    onClick = { onOptionSelected("Orange") }
                )
            }
        }
    }
}

@Composable
fun OptionItem(
    imageRes: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .padding(end = 16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun HintBar(hintText: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = hintText,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLessonsScreen() {
    LessonsScreen(
        onBackButtonClicked = {},
        onOptionSelected = { option -> println("Selected: $option") }
    )
}
