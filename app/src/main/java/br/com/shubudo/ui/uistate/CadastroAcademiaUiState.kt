package br.com.shubudo.ui.uistate

import br.com.shubudo.model.Filial

sealed class CadastroAcademiaUiState {
    object Loading : CadastroAcademiaUiState()

    data class Error(val message: String) : CadastroAcademiaUiState()

    data class Success(
        val nome: String = "",
        val descricao: String = "",
        val filiais: List<Filial> = emptyList(),
        val id: String? = null,
        val isEditando: Boolean = false
    ) : CadastroAcademiaUiState()
}
