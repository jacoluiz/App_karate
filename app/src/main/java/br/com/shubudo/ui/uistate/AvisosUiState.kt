package br.com.shubudo.ui.uistate

import br.com.shubudo.model.Aviso

sealed class AvisosUiState {
    object Loading : AvisosUiState()
    object Empty : AvisosUiState()
    data class Success(val avisos: List<Aviso>) : AvisosUiState()
}