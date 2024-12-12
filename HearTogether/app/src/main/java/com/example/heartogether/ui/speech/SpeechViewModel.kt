package com.example.heartogether.ui.speech

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.heartogether.HearTogether
import com.example.heartogether.data.network.ResponseMisPronun
import com.example.heartogether.data.network.ResponsePostRequest
import com.example.heartogether.repository.MisProNunRepo
import com.example.heartogether.ui.home.MisPronunUiState
import com.example.heartogether.ui.home.MisPronunViewModel
import com.example.heartogether.ui.home.PostRequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SpeechViewModel(
    private val misPronunRepo : MisProNunRepo): ViewModel() {


        private val _stateSTT = MutableStateFlow<SpeechUiState>(SpeechUiState.Loading)
        val stateSTT = _stateSTT.asStateFlow()

        private val _textSTT = MutableStateFlow<String>("")
        val textSTT = _textSTT.asStateFlow()


        fun SpeechToText(filePath : String) {
            viewModelScope.launch (Dispatchers.IO){
                _stateSTT.value = SpeechUiState.Loading
                Log.d("stateSTT", "${stateSTT.value}")
                val result = misPronunRepo.callSpeechToText(filePath)
                _stateSTT.value = result.let {
                    _textSTT.value = result
                    Log.d("textSTT", "${_textSTT.value}")
                    SpeechUiState.Success
                } ?: SpeechUiState.Error
                Log.d("stateSTT", "${stateSTT.value}")
                Log.d("textSTT", "${_textSTT.value}")


            }
        }
    fun setText(text : String) {
        _textSTT.value = text
    }

    companion object {
        val Factory2: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as HearTogether)
                val misPronunRepo = application.container
                SpeechViewModel(misPronunRepo = misPronunRepo)
            }
        }
    }

}