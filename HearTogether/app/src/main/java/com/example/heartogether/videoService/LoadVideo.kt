package com.example.heartogether.videoService


import androidx.annotation.OptIn
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerWithBuffering(url: String) {
    val context = LocalContext.current

    // Create ExoPlayer instance with LoadControl
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setLoadControl(
                DefaultLoadControl.Builder()
                    .setBufferDurationsMs(
                        15000, // Minimum buffer duration (15s)
                        30000, // Maximum buffer duration (30s)
                        1500,  // Playback start buffer (1.5s)
                        5000   // Playback resume buffer (5s)
                    ).build()
            )
            .build().apply {
                setMediaItem(MediaItem.fromUri(url))
                prepare()
            }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Default aspect ratio (16:9)
    val aspectRatio = 16f / 9f

    // Display PlayerView inside Jetpack Compose with AspectRatio
    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = true // Show playback controls
            }
        },
        modifier = Modifier
            .fillMaxWidth()  // Make sure it fills available width
            .aspectRatio(aspectRatio)  // Keep aspect ratio intact
    )
}
