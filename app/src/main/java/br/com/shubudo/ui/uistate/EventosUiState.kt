package br.com.shubudo.ui.uistate

import br.com.shubudo.model.Evento

sealed class EventosUiState {
    object Loading : EventosUiState()
    object Empty : EventosUiState()
    data class Success(
        val eventosAgrupados: Map<String, List<Evento>>
    ) : EventosUiState()
}

