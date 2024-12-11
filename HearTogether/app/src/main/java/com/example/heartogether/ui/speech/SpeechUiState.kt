package com.example.heartogether.ui.speech

import com.example.heartogether.data.network.ResponseMisPronun
import com.example.heartogether.data.network.ResponsePostRequest
import com.example.heartogether.ui.home.MisPronunUiState

sealed interface SpeechUiState {
    object Loading : SpeechUiState
    object Error : SpeechUiState
    object Success : SpeechUiState
}