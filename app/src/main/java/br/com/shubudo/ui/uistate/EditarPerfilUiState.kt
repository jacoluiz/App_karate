package br.com.shubudo.ui.uistate

sealed class EditarPerfilUiState {
    // Estado de carregamento
    object Loading : EditarPerfilUiState()

    // Estado vazio (quando não há dados do usuário para editar)
    object Empty : EditarPerfilUiState()

    // Estado de sucesso com os dados do usuário para edição
    data class Success(
        val nome: String = "",
        val username: String = "",
        val email: String = "",
        val corFaixa: String = "",
        val idade: String = "",
        val peso: String = "",
        val altura: String = "",
        val dan: Int = 0,
        val academia: String = "",
        val tamanhoFaixa: String = "",
        val lesaoOuLaudosMedicos: String? = "",
        val registroAKSD: String? = "",
        val perfil: String = "",
    ) : EditarPerfilUiState()
}