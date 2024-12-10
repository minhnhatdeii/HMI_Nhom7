package com.example.heartogether.ui.home

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MisPronunViewModel(
    private val misPronunRepo : MisProNunRepo
) : ViewModel() {
    private val _state = MutableStateFlow<MisPronunUiState>(MisPronunUiState.Loading)
    val state = _state.asStateFlow()

    private val _statePost = MutableStateFlow<PostRequestState>(PostRequestState.Default)
    val statePost = _statePost.asStateFlow()

    private val _difMode = MutableStateFlow<Int>(1) // Giá trị mặc định
    val difMode = _difMode.asStateFlow()

    private val _dataDefault = MutableStateFlow<ResponseMisPronun>(ResponseMisPronun("", "")) // Giá trị mặc định
    val dataDefault = _dataDefault.asStateFlow()

    private val _dataUpdate = MutableStateFlow<ResponsePostRequest>(ResponsePostRequest("", "0","","","")) // Giá trị mặc định
    val dataUpdate = _dataUpdate.asStateFlow()

    private val _isCheckPostRequest = MutableStateFlow<Boolean>(false) // Giá trị mặc định
    val isCheckPostRequest = _isCheckPostRequest.asStateFlow()

    private val _mediaPLayer = MutableStateFlow<MediaPlayer>(MediaPlayer()) // Giá trị mặc định
    val mediaPlayer = _mediaPLayer.asStateFlow()

    init {
        defaultData(_difMode.value)
    }

    fun initMedia() {
        _mediaPLayer.value = MediaPlayer()
    }
    fun defaultData(mode : Int ?= _difMode.value) {
        viewModelScope.launch (Dispatchers.IO){
            _dataUpdate.value = ResponsePostRequest("", "0","","","")
            _state.value = MisPronunUiState.Loading
            _statePost.value = PostRequestState.Default
            _difMode.value = mode!!
            _isCheckPostRequest.value = false
            val result = misPronunRepo.updateMisPronunScreenData(_difMode.value)
            _dataDefault.value = result!!
            _state.value = result?.let {
                MisPronunUiState.Success(it)
            } ?: MisPronunUiState.Error
        }
    }

    fun postRequest(
        sentence : String?= _dataDefault.value.sentence,
        audio: String ?= _dataDefault.value.ipaSentence) {
        viewModelScope.launch (Dispatchers.IO) {
            _statePost.value = PostRequestState.Loading
            val result = misPronunRepo.postRequest(sentence!!, audio!!) // Gọi API update

            _statePost.value = result?.let {
                PostRequestState.Success
            } ?: PostRequestState.Error
            _dataUpdate.value = result?.let {
                it
            } ?:_dataUpdate.value
        }
    }

    fun changeIsCheckPostReQuest() {
        if (_isCheckPostRequest.value == false) {
            _isCheckPostRequest.value = true
        }
    }

    fun setDifMode(num : Int) {
        _difMode.value = num
    }



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as HearTogether)
                val misPronunRepo = application.container
                MisPronunViewModel(misPronunRepo = misPronunRepo)
            }
        }
    }

}