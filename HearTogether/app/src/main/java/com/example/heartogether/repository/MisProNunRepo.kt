package com.example.heartogether.repository

import androidx.media3.exoplayer.ExoPlayer
import com.example.heartogether.data.network.ResponseMisPronun
import com.example.heartogether.data.network.ResponsePostRequest

interface MisProNunRepo {
    suspend fun updateMisPronunScreenData(difMode: Int) : ResponseMisPronun?
    suspend fun postRequest(curTranscript : String, audio : String) : ResponsePostRequest?
    suspend fun callSpeechToText(filePath : String) : String
}