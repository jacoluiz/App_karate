package br.com.shubudo.ui.uistate

import br.com.shubudo.model.Aviso

sealed class AvisoUiState {

    object Loading : AvisoUiState()

    object Empty : AvisoUiState()

    data class Success(
        val avisos: List<Aviso> = emptyList()
    ) : AvisoUiState()
}
