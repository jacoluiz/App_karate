package br.com.shubudo.ui.uistate

import br.com.shubudo.model.Aviso

sealed class DetalheAvisoUiState {
    object Loading : DetalheAvisoUiState()
    data class Success(val aviso: Aviso) : DetalheAvisoUiState()
    data class Error(val message: String) : DetalheAvisoUiState()
}
