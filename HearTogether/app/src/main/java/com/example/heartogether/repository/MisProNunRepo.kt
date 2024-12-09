package com.example.heartogether.repository

import com.example.heartogether.data.network.ResponseMisPronun
import com.example.heartogether.data.network.ResponsePostRequest

interface MisProNunRepo {
    suspend fun updateMisPronunScreenData(difMode: Int) : ResponseMisPronun?
    suspend fun postRequest(curTranscript : String, audio : String) : ResponsePostRequest?
}