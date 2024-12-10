package com.example.heartogether.ui.speech

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SpeechScreenUI() {
    // State cho Text
    var inputText by remember { mutableStateOf("") }

    // Thiết kế giao diện màn hình
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Tiêu đề
        Text(
            text = "Màn hình Text-to-Speech và Speech-to-Text",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Text Field để nhập văn bản
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Nhập văn bản") },
            modifier = Modifier.fillMaxWidth()
        )

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

        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị kết quả nhận diện giọng nói (nếu có)
        Text(
            text = "Kết quả nhận diện: (Kết quả sẽ hiển thị ở đây)",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSpeechScreen() {
    SpeechScreenUI()
}
