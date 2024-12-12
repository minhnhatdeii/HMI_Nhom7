package com.example.heartogether.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heartogether.R

@Composable
fun CategoryItem(
    iconRes: Int,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable{onClick()},
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF9AD983))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    onBackClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onCategory2Click: () -> Unit,
    onCategory3Click: () -> Unit,
    onCategory4Click: () -> Unit,
    onCategory5Click: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Training Sign Language") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Let's try ur best!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            CategoryItem(iconRes = R.drawable.lession1, title = "Lesson 1", onClick = onCategoryClick)
            CategoryItem(iconRes = R.drawable.lession2, title = "Lesson 2",onClick = onCategory2Click)
            CategoryItem(iconRes = R.drawable.lession3, title = "Lesson 3",onClick = onCategory3Click)
            CategoryItem(iconRes = R.drawable.lession4, title = "Lesson 4",onClick = onCategory4Click)
            CategoryItem(iconRes = R.drawable.lession5, title = "Lesson 5",onClick = onCategory5Click)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewCategoryScreen() {
//    CategoryScreen(onBackClick = {})
//}
