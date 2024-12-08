package com.example.heartogether.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.heartogether.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MispronounceScreen(
    onBackButtonClicked: () -> Unit
) {
    var selectedDifficulty by remember { mutableStateOf("Medium") }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onBackButtonClicked() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back Icon")
                    }
                },
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Mispronounce",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.align(Alignment.Center) // Căn giữa tiêu đề
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(420.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Color.Green)) {
                                    append("Days are ")
                                }
                                withStyle(style = SpanStyle(color = Color.Red)) {
                                    append("beginning ")
                                }
                                withStyle(style = SpanStyle(color = Color.Green)) {
                                    append("to get shorter again.")
                                }
                            },
                            style = MaterialTheme.typography.headlineSmall // Tăng kích thước chữ
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "/ deɪz ɑr brɪ'gɪnɪŋ tʊ gɪt 'ʃɔrtər ə'gen. /",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Phần "You"
                        Text("You:", style = MaterialTheme.typography.headlineSmall)
                        Text(
                            text = "/ daɪz ɑr brɪ'gɪ tʊ gɪt 'ʃɔrtə gen. /",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Blue
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, bottom = 0.dp)
                        ) {
                            Text(
                                text = "Score: 10", // Giả sử điểm là 10
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.align(Alignment.BottomStart)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Card chứa thanh độ khó
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        shape = MaterialTheme.shapes.large,
                        elevation = CardDefaults.cardElevation(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)  // Chiều cao của Row của thanh độ khó
                                .padding(0.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DifficultyButton(
                                label = "Rand",
                                isSelected = selectedDifficulty == "Rand",
                                onClick = { selectedDifficulty = "Rand" }
                            )
                            DifficultyButton(
                                label = "Easy",
                                isSelected = selectedDifficulty == "Easy",
                                onClick = { selectedDifficulty = "Easy" }
                            )
                            DifficultyButton(
                                label = "Medium",
                                isSelected = selectedDifficulty == "Medium",
                                onClick = { selectedDifficulty = "Medium" }
                            )
                            DifficultyButton(
                                label = "Hard",
                                isSelected = selectedDifficulty == "Hard",
                                onClick = { selectedDifficulty = "Hard" }
                            )
                        }
                    }

                    Button(
                        onClick = { /* Handle New button */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .height(48.dp)
                    ) {
                        Text(text = "New", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { /* Handle mic click */ },
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.Transparent, CircleShape),
                        content = {
                            Image(
                                painter = painterResource(id = R.drawable.heartogether),
                                contentDescription = "Mic",
                                modifier = Modifier.size(80.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Hold to speak",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun DifficultyButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFF9AD983) else Color.Transparent
    val textColor = if (isSelected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(backgroundColor, shape = MaterialTheme.shapes.small)
            .height(48.dp)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
