package com.example.heartogether.ui.dictionary

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.heartogether.data.dictionary.DictionaryData
import com.example.heartogether.data.network.WordData
import com.example.heartogether.ui.theme.NotoSans
import com.example.heartogether.videoService.VideoPlayerWithBuffering

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignDictionaryScreen() {
    var query by remember { mutableStateOf("") }
    var selectedSign by remember { mutableStateOf<WordData?>(null) }
    val searchResults by remember(query) {
        derivedStateOf { DictionaryData!!.filter { it.wordKey.startsWith(query, ignoreCase = true) }.take(9) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dictionary",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontFamily = NotoSans,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TextField(
                value = query,
                onValueChange = {
                    query = it
                    selectedSign = null
                },
                placeholder = { Text(text = "Type sign or word...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = {
                            query = ""
                            selectedSign = null
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    containerColor = Color.Gray.copy(alpha = 0.2f)
                ),
                singleLine = true,
                textStyle = TextStyle(fontSize = 18.sp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            selectedSign?.let {
                Text(
                    text = "Sign for: ${it.wordKey}",
                    fontSize = 24.sp,
                    fontFamily = NotoSans,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                VideoPlayerWithBuffering(
                    it.movieUrl
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Synonyms : ${it.wordList.joinToString(separator = ", ")}",
                    fontSize = 16.sp,
                    fontFamily = NotoSans,
                    color = Color.Black
                )

            }

            if (selectedSign == null) {
                Text(
                    text = if (query.isEmpty()) "All Signs" else "Suggestions for \"$query\"",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = NotoSans,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(searchResults) { suggestion ->
                        SuggestionCard(
                            suggestion = suggestion.wordKey,
                            onClick = {
                                selectedSign = suggestion
                            }
                        )
                    }
                }
            }

        }
    }
}



@Composable
fun SuggestionCard(suggestion: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF9AD983))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = suggestion,
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = NotoSans
            )
            Icon(Icons.Default.Search, contentDescription = "View Sign", tint = Color.Gray)
        }
    }
}

@Preview
@Composable
fun PreviewSignDictionarySearchScreen() {
    SignDictionaryScreen()
}
