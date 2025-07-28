package br.com.shubudo.ui.uistate

sealed class UploadUiState {
    object Idle : UploadUiState()
    data class Uploading(val progress: Float) : UploadUiState()
    object Success : UploadUiState()
    data class Error(val message: String) : UploadUiState()
}