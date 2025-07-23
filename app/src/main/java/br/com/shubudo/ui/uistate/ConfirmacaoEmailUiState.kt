package br.com.shubudo.ui.uistate

sealed class ConfirmacaoEmailUiState {
    object Idle : ConfirmacaoEmailUiState()
    object Loading : ConfirmacaoEmailUiState()
    data class Success(val message: String) : ConfirmacaoEmailUiState()
    data class Error(val error: String) : ConfirmacaoEmailUiState()
    data class LoginFailedAfterConfirmation(val error: String) : ConfirmacaoEmailUiState()
}
