package br.com.shubudo.ui.uistate

import android.net.Uri

sealed class RelatorioUiState {
    object Idle : RelatorioUiState()
    object Downloading : RelatorioUiState()
    data class Success(val uri: Uri, val fileName: String) : RelatorioUiState()
    data class Error(val message: String) : RelatorioUiState()
}