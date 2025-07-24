package br.com.shubudo.ui.uistate

import androidx.compose.ui.text.input.TextFieldValue

sealed class CadastroEventoUiState {
    object Loading : CadastroEventoUiState()

    data class Form(
        val titulo: String = "",
        val descricao: String = "",
        val local: String = "",
        val data: TextFieldValue = TextFieldValue(""),
        val horario: TextFieldValue = TextFieldValue(""),
        val tituloError: String? = null,
        val localError: String? = null,
        val dataError: String? = null,
        val horarioError: String? = null,
        val isLoading: Boolean = false
    ) : CadastroEventoUiState()
}