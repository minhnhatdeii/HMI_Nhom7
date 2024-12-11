package com.example.heartogether.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heartogether.R
import com.example.heartogether.data.network.WordData
import com.example.heartogether.ui.theme.NotoSans
import com.example.heartogether.videoService.VideoPlayerWithBuffering
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonsScreen(
    index: Int,
    dictionaryData: List<WordData>,
    onBackButtonClicked: () -> Unit,
    onOptionSelected: (String) -> Unit,
    onNextClicked: () -> Unit
) {
    val wordsToDisplay = dictionaryData.subList(index, (index + 10).coerceAtMost(dictionaryData.size))

    var currentIndex by remember { mutableStateOf(0) }
    val currentWord = wordsToDisplay.getOrNull(currentIndex)


    LaunchedEffect(currentIndex, currentWord) {
        Log.d("LessonsScreen", "currentIndex: $currentIndex")
        Log.d("LessonsScreen", "currentWord: ${currentWord?.movieUrl}")
    }

    var randomString1: String
    var randomString2: String

    if (currentWord != null) {
        if (Random.nextBoolean()) {
            randomString1 = currentWord.wordKey
            randomString2 = currentWord.region
        } else {
            randomString2 = currentWord.wordKey
            randomString1 = currentWord.region
        }
    } else {
        randomString1 = "Default String 1"
        randomString2 = "Default String 2"
    }

    var selectedOption by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableStateOf(0) }

    val isFinished = currentIndex >= 9

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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (!isFinished) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Which is this?",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    if (currentWord != null) {
                        VideoPlayerWithBuffering(currentWord.movieUrl)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OptionCard(
                            text = randomString1,
                            isCorrect = selectedOption == currentWord?.wordKey,
                            isSelected = selectedOption == randomString1,
                            onClick = {
                                if (selectedOption == null) {
                                    selectedOption = randomString1
                                    onOptionSelected(randomString1)
                                    if (randomString1 == currentWord?.wordKey) {
                                        score += 10
                                    }
                                }
                            }
                        )
                        OptionCard(
                            text = randomString2,
                            isCorrect = selectedOption == currentWord?.wordKey,
                            isSelected = selectedOption == randomString2,
                            onClick = {
                                if (selectedOption == null) {
                                    selectedOption = randomString2
                                    onOptionSelected(randomString2)
                                    if (randomString2 == currentWord?.wordKey) {
                                        score += 10
                                    }
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Score: $score",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Button(
                    onClick = {
                        currentIndex = (currentIndex + 1)
                        selectedOption = null
                        onNextClicked()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF9AD983)
                    )
                ) {
                    Text(text = "Next")
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_5),
                        contentDescription = "Congratulations",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .size(500.dp)
                    )
                    Text(
                        text = "Congratulations! You've completed the Lesson!",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = NotoSans,
                            fontSize = 16.sp
                        ),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "Final Score: $score",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}





@Composable
fun OptionCard(
    text: String,
    isCorrect: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected && isCorrect -> Color(0xFF9AD983) // Màu xanh khi đúng
        isSelected && !isCorrect -> Color(0xFFF44336) // Màu đỏ khi sai
        else -> MaterialTheme.colorScheme.surface // Màu mặc định
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
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

//@Preview(showBackground = true)
//@Composable
//fun PreviewLessonsScreen() {
//    LessonsScreen(
//        onBackButtonClicked = {},
//        onOptionSelected = { option -> println("Selected: $option") }
//    )
//}
