package br.com.shubudo.ui.uistate

import br.com.shubudo.model.Parceiro

sealed class ParceirosUiState {
    object Loading : ParceirosUiState()
    object Empty : ParceirosUiState()
    data class Success(val parceiros: List<Parceiro>) : ParceirosUiState()
    data class Error(val mensagem: String) : ParceirosUiState()
}
