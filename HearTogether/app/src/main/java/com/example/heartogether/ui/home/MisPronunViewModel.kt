package com.example.heartogether.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.heartogether.HearTogether
import com.example.heartogether.repository.MisProNunRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MisPronunViewModel(
    private val misPronunRepo : MisProNunRepo
) : ViewModel() {
    private val _state = MutableStateFlow<MisPronunUiState>(MisPronunUiState.Loading)
    val state = _state.asStateFlow()

    private val _difMode = MutableStateFlow<Int>(1) // Giá trị mặc định
    val difMode = _difMode.asStateFlow()

    init {
        defaulData()
    }

    fun defaulData() {
        viewModelScope.launch (Dispatchers.IO){
            _state.value = MisPronunUiState.Loading
            val result = misPronunRepo.updateMisPronunScreenData(difMode.value)

            _state.value = result?.let {
                MisPronunUiState.Success(it)
            } ?: MisPronunUiState.Error
        }
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