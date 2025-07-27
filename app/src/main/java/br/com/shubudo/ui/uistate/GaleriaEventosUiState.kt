package br.com.shubudo.ui.uistate

import br.com.shubudo.model.GaleriaEvento

sealed class GaleriaEventosUiState {
    object Loading : GaleriaEventosUiState()
    data class Success(val eventos: List<GaleriaEvento>) : GaleriaEventosUiState()
    object Empty : GaleriaEventosUiState()
    data class Error(val mensagem: String) : GaleriaEventosUiState()

}
