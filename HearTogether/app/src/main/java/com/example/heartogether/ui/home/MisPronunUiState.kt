package com.example.heartogether.ui.home

import com.example.heartogether.data.network.ResponseMisPronun
import com.example.heartogether.data.network.ResponsePostRequest


sealed interface MisPronunUiState {
    object Loading : MisPronunUiState
    object Error : MisPronunUiState
    data class Success(
        val getSample: ResponseMisPronun,
        val newData: ResponsePostRequest?= null
    ) : MisPronunUiState
}

sealed interface PostRequestState {
    object Default : PostRequestState
    object Loading : PostRequestState
    object Error : PostRequestState
    object Success : PostRequestState
}
