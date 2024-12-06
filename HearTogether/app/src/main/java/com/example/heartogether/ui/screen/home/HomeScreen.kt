package com.example.heartogether.ui.screen.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heartogether.R

@Composable
fun HomeScreen(userName: String = "Jack") {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Open menu */ }) {
                Image(
                    painter = painterResource(id = R.drawable.icons8_menu_100),
                    contentDescription = "Menu"
                )
            }
            IconButton(onClick = { /* Notifications */ }) {
                Image(
                    painter = painterResource(id = R.drawable.icons8_more_100),
                    contentDescription = "Notifications"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Greeting message
        Text(
            text = "Nice to meet you, ${userName}!",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )
        Text(
            text = "Let’s workout today!",
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LessonCard(
                title = "Training Sign Language",
                description = "Study sign language!",
                backgroundColor = Color(0xFF9AD983), // Màu xanh lá nhạt
                imageRes = R.drawable.__ways_to_bring_sign_language_into_your_classroom// Hình ảnh riêng cho bài học này
            )
            LessonCard(
                title = "Training Your Pronunciation",
                description = "Improve your pronunciation!",
                backgroundColor = Color(0xFF9AD983), // Màu xanh lá nhạt
                imageRes = R.drawable.lets_talk_3_1280x640 // Hình ảnh riêng cho bài học này
            )
        }
    }

}

@Composable
fun LessonCard(
    title: String,
    description: String,
    backgroundColor: Color,
    imageRes: Int // Hình ảnh riêng cho mỗi bài học
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Lesson Icon",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Text(
                    text = description,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
