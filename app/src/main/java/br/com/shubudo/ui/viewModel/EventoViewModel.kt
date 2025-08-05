package br.com.shubudo.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager.idAcademiaVisualizacao
import br.com.shubudo.model.Evento
import br.com.shubudo.repositories.EventoRepository
import br.com.shubudo.ui.uistate.EventosUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventoViewModel @Inject constructor(
    private val eventoRepository: EventoRepository
) : ViewModel() {

    private val _eventosUiState = MutableStateFlow<EventosUiState>(EventosUiState.Loading)
    val eventosUiState: StateFlow<EventosUiState> = _eventosUiState.asStateFlow()

    private var fetchJob: Job? = null

    init {
        recarregarEventos()
    }

    fun recarregarEventos() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            eventoRepository.refreshEventos()
            eventoRepository.getEventos()
                .collectLatest { eventos ->
                    // Filtra eventos da academia atual
                    val eventosDaAcademia = eventos.filter { it.academia == idAcademiaVisualizacao }

                    if (eventosDaAcademia.isEmpty()) {
                        _eventosUiState.update { EventosUiState.Empty }
                    } else {
                        val agrupadoPorData = eventosDaAcademia.groupBy {
                            it.dataInicio.substring(0, 10)
                        }
                        _eventosUiState.update {
                            EventosUiState.Success(eventosAgrupados = agrupadoPorData)
                        }
                    }
                }
        }
    }


    fun deletarEvento(eventoId: String) {
        viewModelScope.launch {
            try {
                eventoRepository.deletarEvento(eventoId)
                // Atualiza lista manualmente após a exclusão
                val estadoAtual = _eventosUiState.value
                if (estadoAtual is EventosUiState.Success) {
                    val eventosAtualizados = estadoAtual.eventosAgrupados.values.flatten()
                        .filterNot { it._id == eventoId }

                    if (eventosAtualizados.isEmpty()) {
                        _eventosUiState.update { EventosUiState.Empty }
                    } else {
                        val novoAgrupamento = eventosAtualizados.groupBy {
                            it.dataInicio.substring(0, 10)
                        }
                        _eventosUiState.update {
                            EventosUiState.Success(eventosAgrupados = novoAgrupamento)
                        }
                    }
                }
            } catch (e: Exception) {
                // Log do erro, mas não altera o estado
            }
        }
    }
}
