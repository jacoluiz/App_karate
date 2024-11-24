package br.com.shubudo.ui.uistate

sealed class PerfilUiState {

    // Estado de carregamento
    object Loading : PerfilUiState()

    // Estado vazio (quando não há dados do usuário)
    object Empty : PerfilUiState()

    // Estado de sucesso com os dados do usuário carregados
    data class Success(
        val nome: String = "",
        val username: String = "",
        val email: String = "",
        val corFaixa: String = "",
        val idade: String = "",
        val peso: String = "",
        val altura: String = ""
    ) : PerfilUiState()
}
