package br.com.shubudo.ui.uistate

import br.com.shubudo.model.Programacao

sealed class DetalheFaixaUiState {

    object Loading : DetalheFaixaUiState()

    data class Success(
        val programacao: Programacao
    ) : DetalheFaixaUiState()

    object Empty : DetalheFaixaUiState()
}