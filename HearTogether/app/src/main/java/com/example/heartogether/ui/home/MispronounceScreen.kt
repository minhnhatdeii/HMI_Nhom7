package com.example.heartogether.ui.home

import android.media.MediaPlayer
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.heartogether.R
import com.example.heartogether.data.network.ResponseMisPronun
import com.example.heartogether.services.AudioService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.IOException

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("Circular Progress Indicator"),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.Blue)
    }
}

@Composable
fun ErrorScreen(
    onRefreshContent: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("Error"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_connection_error),
                contentDescription = stringResource(R.string.connection_error)
            )
            IconButton(onClick = onRefreshContent) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.refresh)
                )
            }
        }
    }
}
private fun decodeUnicode(input: String): String {
    return Regex("\\\\u([0-9A-Fa-f]{4})").replace(input) {
        val codePoint = it.groupValues[1].toInt(16)
        codePoint.toChar().toString()
    }
}

private fun play2(path: String?, mediaPlayer: MediaPlayer) {
    CoroutineScope(Dispatchers.IO).launch {
        if (path.isNullOrEmpty()) {
            Log.d("TAG", "play2: File path is null or empty.")
            return@launch
        }

        try {
            mediaPlayer.reset() // Đưa MediaPlayer về trạng thái Idle
            mediaPlayer.setDataSource(path) // Thiết lập file audio
            mediaPlayer.setOnPreparedListener { mp ->
                mp.start() // Phát khi đã chuẩn bị xong
                Log.d("TAG", "MediaPlayer started playing.")
            }
            mediaPlayer.prepareAsync() // Chuẩn bị không đồng bộ
        } catch (e: IllegalArgumentException) {
            Log.e("TAG", "Invalid arguments for MediaPlayer: ${e.message}")
        } catch (e: IOException) {
            Log.e("TAG", "IO Exception while setting up MediaPlayer: ${e.message}")
        } catch (e: IllegalStateException) {
            Log.e("TAG", "Illegal state for MediaPlayer: ${e.message}")
        } catch (e: Exception) {
            Log.e("TAG", "Unexpected error: ${e.message}")
            e.printStackTrace()
        }
    }
}

private fun encodeM4aToBase64(file: File): String {
    return FileInputStream(file).use { inputStream ->
        val bytes = inputStream.readBytes() // Đọc file thành byte array
        Base64.encodeToString(bytes, Base64.DEFAULT) // Chuyển sang Base64
    }
}
private fun ConvertAndDisplayBase64(filePath: String) : String{
    val file = File(filePath)
    val base64String =
        if (file.exists()) encodeM4aToBase64(file)
        else "Request failed"
    Log.d("base64", "${base64String}")
    return "data:audio/ogg;base64,$base64String"
}

@Composable
fun MispronounceScreen(
    onBackButtonClicked: () -> Unit,
    mService: AudioService?,
    viewModel: MisPronunViewModel = viewModel(factory = MisPronunViewModel.Factory)
) {

    val misPronunUiState = viewModel.state.collectAsStateWithLifecycle().value
    when (misPronunUiState) {
        is MisPronunUiState.Loading -> {
            LoadingScreen()
        }

        is MisPronunUiState.Error -> {
            ErrorScreen(
                onRefreshContent = { viewModel.defaulData() }
            )
        }

        is MisPronunUiState.Success -> {
            // Truy cập vào thuộc tính popularItem khi trạng thái là Success
            val defaultData = misPronunUiState.getSample
            MainMispronounceScreen (
                onBackButtonClicked = onBackButtonClicked,
                mService = mService,
                dataMisPronun = defaultData
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMispronounceScreen(
    onBackButtonClicked: () -> Unit,
    mService: AudioService?,
    dataMisPronun : ResponseMisPronun
) {
    var selectedDifficulty by remember { mutableStateOf("Medium") }
    var mediaPlayer: MediaPlayer? by remember {
        mutableStateOf(null)
    }
    var isCheckPostRequest by remember { mutableStateOf(false) }
    var difficultyValue by remember { mutableStateOf(2) }
    Log.d("TAG44","$mService")
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
                    Box(
                        modifier = Modifier.fillMaxSize() // Đảm bảo Box chiếm toàn bộ không gian Card
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            TextChange(dataMisPronun.sentence, isCheckPostRequest)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = decodeUnicode(dataMisPronun.ipaSentence),
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

                            Button(
                                onClick = {
                                    Log.d("PLAYAUDIO", "${mService?.getRecordedFilePath()}")
                                    mediaPlayer = MediaPlayer()
                                    play2(mService?.getRecordedFilePath(), mediaPlayer!!)
                                    ConvertAndDisplayBase64(mService?.getRecordedFilePath()!!)
                                },
                                modifier = Modifier.size(50.dp)
                            ) {
                                Text(
                                    text = "Button"
                                )
                            }
                        }

                        // Căn "Score" xuống đáy bên phải
                        Text(
                            text = "Score: 10", // Điểm giả sử là 10
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .align(Alignment.BottomStart) // Đưa xuống dưới cùng bên phải
                                .padding(16.dp)
                        )
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
                            .padding(8.dp)
                            .border(BorderStroke(2.dp, Color.Red)),
                        shape = MaterialTheme.shapes.large,
                        elevation = CardDefaults.cardElevation(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(0.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DifficultyButton(
                                label = "Rand",
                                isSelected = selectedDifficulty == "Rand",
                                onClick = { selectedDifficulty = "Rand"
                                    difficultyValue = 0
                                }
                            )
                            DifficultyButton(
                                label = "Easy",
                                isSelected = selectedDifficulty == "Easy",
                                onClick = { selectedDifficulty = "Easy"
                                    difficultyValue = 1}
                            )
                            DifficultyButton(
                                label = "Medium",
                                isSelected = selectedDifficulty == "Medium",
                                onClick = { selectedDifficulty = "Medium"
                                difficultyValue = 2}
                            )
                            DifficultyButton(
                                label = "Hard",
                                isSelected = selectedDifficulty == "Hard",
                                onClick = { selectedDifficulty = "Hard"
                                    difficultyValue = 3}
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
                        onClick = {
                            val recording = mService?.isRecordingStarted ?: false
                            when {
                                recording -> {
                                    mService?.stopRecording()
                                    mService?.stopRecorderService()
                                }
                                else -> {
                                    mService?.startRecording()
                                }
                            }
                        },
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
                    Log.d("Difficulty Value", "Current difficulty: $difficultyValue")
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

@Composable
fun TextChange(text : String, isCheckPostRequest : Boolean) {
    if (!isCheckPostRequest) {
        Text(
            text = text,
        style = MaterialTheme.typography.headlineSmall // Tăng kích thước chữ
        )
    } else {
        Text (
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

    }
}
