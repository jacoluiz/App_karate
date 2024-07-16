package br.com.shubudo.ui.uistate

import br.com.shubudo.model.Faixa

sealed class ProgramacaoUiState {

    object Loading : ProgramacaoUiState()

    object Empty : ProgramacaoUiState()

    data class Success(
        val faixas: List<Faixa> = emptyList()
    ) : ProgramacaoUiState()

}