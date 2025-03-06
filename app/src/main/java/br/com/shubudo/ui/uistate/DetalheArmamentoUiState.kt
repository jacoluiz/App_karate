package br.com.shubudo.ui.uistate

import br.com.shubudo.model.Armamento

sealed class DetalheArmamentoUiState {
    object Loading : DetalheArmamentoUiState()
    data class Success(val armamento: Armamento) : DetalheArmamentoUiState()
    data class Error(val message: String) : DetalheArmamentoUiState()
}
