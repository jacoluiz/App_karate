package br.com.shubudo.ui.uistate

sealed class CadastroUiState {
    object Idle : CadastroUiState() // Estado inicial
    object Loading : CadastroUiState() // Quando está carregando
    data class Success(val message: String) : CadastroUiState() // Cadastro bem-sucedido
    data class Error(val error: String) : CadastroUiState() // Cadastro com erro
}
