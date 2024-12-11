package com.example.heartogether.repository

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.layout.ColumnScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.heartogether.R
import com.example.heartogether.data.network.WordData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.File

fun loadWordData(context: Context): MutableList<WordData>? {
    Log.d("Tag","${R.raw.data}")
    val json = context.resources.openRawResource(R.raw.data).bufferedReader().use { it.readText() }
    val type = object : TypeToken<List<WordData>>() {}.type
    return Gson().fromJson(json, type)
}
fun callTextToSpeechApi(inputText: String, context: Context) {
    // Prepare the request body
    val requestBody = JSONObject().apply {
        put("text", inputText)
        put("lang", "en")
        put("speed", "fast")
    }.toString()

    val client = OkHttpClient()

    val mediaType = "application/json".toMediaTypeOrNull()
    val body = RequestBody.create(mediaType, requestBody)
    val request = Request.Builder()
        .url("https://text-to-speach-api.p.rapidapi.com/text-to-speech")
        .post(body)
        .addHeader("x-rapidapi-key", "1ec5c83b55mshd15a7035beade6fp12f050jsn8791b1a67393")
        .addHeader("x-rapidapi-host", "text-to-speach-api.p.rapidapi.com")
        .addHeader("Content-Type", "application/json")
        .build()

    // Execute the API call in a background thread
    CoroutineScope(Dispatchers.IO).launch {
        try {
            Log.d("TTS2", "dawdawd")

            val response = client.newCall(request).execute()
            Log.d("TTS2", "${response.isSuccessful}")
            if (response.isSuccessful) {
                // Get raw audio response directly

                val byteArray = response.body?.bytes() // Get the byte array directly

                byteArray?.let {
                    // Save the byteArray to a temporary file and play it with MediaPlayer
                    val tempFile = File.createTempFile("audio", ".mp3", context.cacheDir)
                    tempFile.writeBytes(it)

                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource(tempFile.absolutePath)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                }
            } else {
                // Handle the error case
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

