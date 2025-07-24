package br.com.shubudo.ui.uistate

import br.com.shubudo.model.Usuario

sealed class CadastroAvisoUiState {
    object Loading : CadastroAvisoUiState()
    data class Success(
        val titulo: String = "",
        val conteudo: String = "",
        val publicoAlvo: List<String> = emptyList(),
        val isEditando: Boolean = false,
        val usuarios: List<Usuario>,
        val usuariosFiltrados: List<Usuario>,
        val avisoId: String? = null,
        val usuariosSelecionados: Set<Usuario> = emptySet()
    ) : CadastroAvisoUiState()

    data class Error(val message: String) : CadastroAvisoUiState()
}