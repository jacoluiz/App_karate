package br.com.shubudo.ui.uistate

import br.com.shubudo.model.Academia

sealed class EditarPerfilUiState {
    // Estado de carregamento
    object Loading : EditarPerfilUiState()

    // Estado vazio (quando não há dados do usuário para editar)
    object Empty : EditarPerfilUiState()

    // Estado de sucesso com os dados do usuário para edição
    data class Success(
        val id: String = "",
        val nome: String = "",
        val username: String = "",
        val email: String = "",
        val corFaixa: String = "",
        val idade: String = "",
        val peso: String = "",
        val altura: String = "",
        val dan: Int = 0,
        val tamanhoFaixa: String = "",
        val lesaoOuLaudosMedicos: String? = "",
        val registroAKSD: String? = "",
        val filialId: String,
        val perfis: List<String> = listOf("aluno"),
        val status: String = "ativo",
        val professorEm: List<String> = listOf(""),
        val academias: List<Academia> = emptyList()
    ) : EditarPerfilUiState()
}