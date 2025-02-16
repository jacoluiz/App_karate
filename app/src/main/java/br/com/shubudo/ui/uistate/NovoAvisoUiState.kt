package br.com.shubudo.ui.uistate

import br.com.shubudo.model.Aviso

sealed class NovoAvisoUiState {
    object Idle : NovoAvisoUiState()
    object Loading : NovoAvisoUiState()
    data class Success(val novoAviso: Aviso) : NovoAvisoUiState()
    data class Error(val message: String) : NovoAvisoUiState()
}
