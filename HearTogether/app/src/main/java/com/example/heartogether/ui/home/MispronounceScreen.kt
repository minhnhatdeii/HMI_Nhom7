package com.example.heartogether.ui.home

import android.media.MediaPlayer
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.BorderStroke
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.heartogether.data.network.ResponsePostRequest
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
        Base64.encodeToString(bytes, Base64.NO_WRAP) // Chuyển sang Base64
    }
}
private fun ConvertAndDisplayBase64(filePath: String) : String{
    val file = File(filePath)
    val base64String =
        if (file.exists()) encodeM4aToBase64(file)
        else "Request failed"
    Log.d("base64", "active")
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
                onRefreshContent = { viewModel.defaultData(1) }
            )
        }

        is MisPronunUiState.Success -> {
            // Truy cập vào thuộc tính popularItem khi trạng thái là Success
            val defaultData = misPronunUiState.getSample
            MainMispronounceScreen (
                onBackButtonClicked = onBackButtonClicked,
                mService = mService,
                dataMisPronun = defaultData,
                viewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMispronounceScreen(
    onBackButtonClicked: () -> Unit,
    mService: AudioService?,
    dataMisPronun : ResponseMisPronun,
    viewModel: MisPronunViewModel
) {
    var selectedDifficulty by remember { mutableStateOf("Medium") }
    var difMode = viewModel.difMode.collectAsState().value
    var mediaPlayer = viewModel.mediaPlayer.collectAsState().value
    var isCheckPostRequest = viewModel.isCheckPostRequest.collectAsState().value
    val postRequestValue = viewModel.dataUpdate.collectAsState().value
    val statePost = viewModel.statePost.collectAsStateWithLifecycle().value
    Log.d("ViewModel", "${statePost}")
    when (difMode) {
        0 -> {
            selectedDifficulty = "Rand"
        }
        1 -> {
            selectedDifficulty = "Easy"
        }
        2 -> {
            selectedDifficulty = "Medium"
        }
        3 -> {
            selectedDifficulty = "Hard"
        }
    }
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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 8.dp, bottom = 8.dp) // Padding thêm để tạo khoảng cách
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                                .align(Alignment.TopStart)
                                .verticalScroll(rememberScrollState())// Nội dung chính ở phần trên
                        ) {

                        //Noi dung sentence
                        TextChange(dataMisPronun.sentence, isCheckPostRequest, postRequestValue.is_letter_correct_all_words)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "/ ${decodeUnicode(dataMisPronun.ipaSentence)} /",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF525252)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                            // Phần "You"
                            Text(
                                "You:",
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.Black
                            )
                            Text(
                                text = "/ ${decodeUnicode(postRequestValue.ipa_transcript)} /",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF005FEE)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomStart)
                                .padding(8.dp), // Thêm khoảng cách với các cạnh
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Score: ${postRequestValue.pronunciation_accuracy}", // Giả sử điểm là 10
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.Black
                            )
                            Button(
                                onClick = {
                                    Log.d("PLAYAUDIO", "${mService?.getRecordedFilePath()}")
                                    viewModel.initMedia()
                                    play2(mService?.getRecordedFilePath(), mediaPlayer!!)
                                    ConvertAndDisplayBase64(mService?.getRecordedFilePath()!!)
                                },
                                modifier = Modifier.size(100.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                )
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.icons8_speaker_96),
                                    contentDescription = "Speaker"
                                )
                            }
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
                    Box(
                        modifier = Modifier
                            .clickable {
                                val randomValue = (1..3).random()
                                viewModel.setDifMode(randomValue)
                                viewModel.defaultData(randomValue)
                            }
                            .background(
                                if (difMode != 0) Color(0xFF9AD983) else Color.Transparent,
                                shape = MaterialTheme.shapes.small
                            )
                            .height(48.dp)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Rand",
                            color = if (difMode != 0) Color.White else Color.Black,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    // Card chứa thanh độ khó
                    Card(
                        modifier = Modifier
                            .weight(4f)
                            .padding(8.dp)
                           ,
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
                                label = "Easy",
                                isSelected = selectedDifficulty == "Easy",
                                onClick = {
                                    selectedDifficulty = "Easy"
                                    viewModel.setDifMode(1)
                                    viewModel.defaultData(1)
                                },
                                modifier = Modifier.weight(1f)
                            )
                            DifficultyButton(
                                label = "Medium",
                                isSelected = selectedDifficulty == "Medium",
                                onClick = {
                                    selectedDifficulty = "Medium"
                                    viewModel.setDifMode(2)
                                    viewModel.defaultData(2)
                                },modifier = Modifier.weight(1f)

                            )
                            DifficultyButton(
                                label = "Hard",
                                isSelected = selectedDifficulty == "Hard",
                                onClick = {
                                    selectedDifficulty = "Hard"
                                    viewModel.setDifMode(3)
                                    viewModel.defaultData(3)
                                },modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clickable {
                                viewModel.defaultData(difMode)
                            }
                            .background(
                                if (difMode != 0) Color(0xFF9AD983) else Color.Transparent,
                                shape = MaterialTheme.shapes.small
                            )
                            .height(48.dp)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "New",
                            color = if (difMode != 0) Color.White else Color.Black,
                            style = MaterialTheme.typography.bodyMedium
                        )
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
                                    viewModel.postRequest(
                                        dataMisPronun.sentence,
                                        ConvertAndDisplayBase64(mService?.getRecordedFilePath()!!)
                                    )
                                    //Thread.sleep(10000)
                                    Log.d("ViewModel2", "${statePost}")
                                }
                                else -> {
                                    mService?.startRecording()
                                }
                            }
                        },
                        modifier = Modifier
                            .size(120.dp)
                            .background(Color(0xFF9AD983), CircleShape),
                        content = {
                            Image(
                                painter = painterResource(id = R.drawable.image_28),
                                contentDescription = "Mic",
                                modifier = Modifier.size(150.dp)  // Phóng to hình ảnh bên trong button
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    )
                }

            }
        }
    }


}



@Composable
fun DifficultyButton(label: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier) {
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
fun TextChange(text : String, isCheckPostRequest : Boolean, mistake : String) {
    if (!isCheckPostRequest) {
        Text(
            text = text,
            color = Color(0xFF000000),
            style = MaterialTheme.typography.headlineSmall // Tăng kích thước chữ
            ,modifier = Modifier.wrapContentHeight()
        )
    } else {
        ColorizeText(text, mistake)

    }
}
@Composable
fun ColorizeText(text: String, bitString: String) {
    val coloredText = buildAnnotatedString {
        val bits = bitString.replace(" ", "") // Loại bỏ khoảng trắng trong chuỗi bit
        val textLength = text.length
        val bitLength = bits.length

        for (i in 0 until textLength) {
            // Xác định màu: Nếu ngoài phạm vi `bits`, mặc định là bit `0` (màu xanh)
            val color = if (i < bitLength && bits[i] == '1') Color.Green else Color.Red

            withStyle(style = SpanStyle(color = color)) {
                append(text[i].toString())
            }
        }
    }

    Text (
        text = coloredText,
        style = MaterialTheme.typography.headlineSmall // Tăng kích thước chữ
    )
}
