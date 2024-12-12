package com.example.heartogether.ui.speech

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import com.example.heartogether.repository.callTextToSpeechApi
import com.example.heartogether.services.AudioService
import com.example.heartogether.ui.home.MisPronunViewModel
import com.example.heartogether.ui.theme.NotoSans

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeechScreen(
    mService: AudioService?,
    viewModel: SpeechViewModel = viewModel(factory = SpeechViewModel.Factory2)
) {
    val textSTT = viewModel.textSTT.collectAsState().value
    var inputText by remember { mutableStateOf(textSTT) }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Speech",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontFamily = NotoSans,
                    )
                },
                modifier = Modifier.height(100.dp)
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
                    value = textSTT,
                    onValueChange = {
                        inputText = it
                        viewModel.setText(it)
                    },
                    label = { Text("Nhập văn bản") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Button để phát âm
            Button(
                onClick = {
                    callTextToSpeechApi(inputText, context)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9AD983),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Phát âm (Text-to-Speech)",
                    fontFamily = NotoSans,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val recording = mService?.isRecordingStarted ?: false
                    when {
                        recording -> {
                            mService?.stopRecording()
                            mService?.stopRecorderService()
                            viewModel.SpeechToText(mService!!.getRecordedFilePath()!!)
                            viewModel.setText(inputText)
                            Log.d("textSTTUI", "${textSTT}")
                            Log.d("textSTTUI", "${inputText}")
                        }

                        else -> {
                            mService?.startRecording()
                            inputText = ""
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9AD983),
                    contentColor = Color.White
                )
            ) {
                Text(text ="Bắt đầu nhận diện giọng nói (Speech-to-Text)",
                    fontFamily = NotoSans,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold

                    )
            }

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewSpeechScreen() {
//    SpeechScreen()
//}
