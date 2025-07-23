package br.com.shubudo.ui.uistate

sealed class EsqueciMinhaSenhaUiState {
    object Idle : EsqueciMinhaSenhaUiState()
    object Loading : EsqueciMinhaSenhaUiState()
    data class Success(val mensagem: String) : EsqueciMinhaSenhaUiState()
    data class Error(val mensagem: String) : EsqueciMinhaSenhaUiState()
}
