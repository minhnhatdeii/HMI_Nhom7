package com.example.heartogether.ui.speech

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeechScreen() {
    var inputText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TTS and STT",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .padding(top = paddingValues.calculateTopPadding(), bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .size(400.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    label = { Text("Nhập văn bản") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button để phát âm
            Button(onClick = { /* TODO: Implement TTS */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Phát âm (Text-to-Speech)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button để bắt đầu nhận dạng giọng nói
            Button(onClick = { /* TODO: Implement STT */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Bắt đầu nhận diện giọng nói (Speech-to-Text)")
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewSpeechScreen() {
//    SpeechScreen()
//}
