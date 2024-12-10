//package com.example.heartogether.ui.home
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Notifications
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import com.example.heartogether.R
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LessonsScreen(
//    onBackButtonClicked: () -> Unit
//) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Training Sign Language") },
//                navigationIcon = {
//                    IconButton(onClick = onBackButtonClicked) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack,
//                            contentDescription = "Back"
//                        )
//                    }
//                },
//                actions = {
//                    IconButton(onClick = { /* TODO: Notifications */ }) {
//                        Icon(
//                            imageVector = Icons.Default.Notifications,
//                            contentDescription = "Notifications"
//                        )
//                    }
//                }
//            )
//        },
//        bottomBar = {
//            HintBar(hintText = "orange hint: like squeezing an orange under chin")
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Which one is orange",
//                style = MaterialTheme.typography.headlineSmall,
//                modifier = Modifier.padding(vertical = 16.dp)
//            )
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                TrainingOption(
//                    imageRes = R.drawable.ic_coffee, // Replace with actual coffee image resource
//                    videoRes = R.drawable.ic_coffee_sign // Replace with actual coffee sign resource
//                )
//                TrainingOption(
//                    imageRes = R.drawable.ic_orange, // Replace with actual orange image resource
//                    videoRes = R.drawable.ic_orange_sign // Replace with actual orange sign resource
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun TrainingOption(imageRes: Int, videoRes: Int) {
//    Column(
//        modifier = Modifier
//            .padding(8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Image(
//            painter = painterResource(id = imageRes),
//            contentDescription = null,
//            modifier = Modifier
//                .weight(1f)
//                .padding(8.dp)
//        )
//        Image(
//            painter = painterResource(id = videoRes),
//            contentDescription = null,
//            modifier = Modifier
//                .weight(1f)
//                .padding(8.dp)
//        )
//    }
//}
//
//@Composable
//fun HintBar(hintText: String) {
//    Surface(
//        color = MaterialTheme.colorScheme.primaryContainer,
//        tonalElevation = 4.dp
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                imageVector = Icons.Default.Notifications,
//                contentDescription = null,
//                tint = MaterialTheme.colorScheme.primary
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                text = hintText,
//                style = MaterialTheme.typography.bodyMedium
//            )
//        }
//    }
//}
