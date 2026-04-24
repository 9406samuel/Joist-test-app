package com.joist.echoapp.presentation

internal sealed class EchoUiState {
    data object Idle : EchoUiState()
    data object Loading : EchoUiState()
    data class Success(val text: String) : EchoUiState()
    data class Error(val message: String) : EchoUiState()
}
