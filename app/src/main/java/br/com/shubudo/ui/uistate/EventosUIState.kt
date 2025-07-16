package br.com.shubudo.ui.uistate

import br.com.shubudo.model.Evento

sealed class EventoUiState {
    object Loading : EventoUiState()
    object Empty : EventoUiState()
    data class Success(
        val eventosAgrupados: Map<String, List<Evento>>
    ) : EventoUiState()
}

