package com.joist.echoapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.joist.echoapp.domain.TextRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class MainViewModel(private val repository: TextRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<EchoUiState>(EchoUiState.Idle)
    val uiState: StateFlow<EchoUiState> = _uiState.asStateFlow()

    fun submit(text: String) {
        if (_uiState.value is EchoUiState.Loading) return
        viewModelScope.launch {
            _uiState.value = EchoUiState.Loading
            repository.validate(text)
                .onSuccess { _uiState.value = EchoUiState.Success(it) }
                .onFailure { _uiState.value = EchoUiState.Error(it.message ?: "Unknown error") }
        }
    }

    companion object {
        internal fun factory(repository: TextRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    MainViewModel(repository) as T
            }
    }
}
