package com.example.heartogether.ui.home

import com.example.heartogether.data.network.ResponseMisPronun
import com.example.heartogether.data.network.ResponsePostRequest


sealed interface MisPronunUiState {
    object Loading : MisPronunUiState
    object Error : MisPronunUiState
    data class Success(
        val getSample: ResponseMisPronun
    ) : MisPronunUiState
}