package br.com.shubudo.ui.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager.idAcademiaVisualizacao
import br.com.shubudo.SessionManager.perfilAtivo
import br.com.shubudo.model.Presenca
import br.com.shubudo.repositories.AcademiaRepository
import br.com.shubudo.repositories.EventoRepository
import br.com.shubudo.ui.uistate.EventosUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventoViewModel @Inject constructor(
    private val eventoRepository: EventoRepository,
    private val academiaRepository: AcademiaRepository
) : ViewModel() {

    private val _eventosUiState = MutableStateFlow<EventosUiState>(EventosUiState.Loading)
    val idParaNomeAcademia = mutableStateOf<Map<String, String>>(emptyMap())
    val eventosUiState: StateFlow<EventosUiState> = _eventosUiState.asStateFlow()

    private var fetchJob: Job? = null

    init {
        recarregarEventos()
    }

    private suspend fun carregarMapaAcademias() {
        val academias = academiaRepository.getAcademias().first()
        idParaNomeAcademia.value = academias.associateBy({ it._id }, { it.nome })
    }

    fun recarregarEventos() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            carregarMapaAcademias()
            eventoRepository.refreshEventos()
            eventoRepository.getEventos()
                .collectLatest { eventos ->

                    val eventosFiltrados = if (perfilAtivo == "adm") {
                        eventos // Mostra todos os eventos
                    } else {
                        eventos.filter {
                            it.academia == idAcademiaVisualizacao || it.eventoOficial
                        }
                    }

                    if (eventosFiltrados.isEmpty()) {
                        _eventosUiState.update { EventosUiState.Empty }
                    } else {
                        val agrupadoPorData = eventosFiltrados.groupBy {
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

    fun confirmarPresencas(eventoId: String, presencas: List<Presenca>) {
        viewModelScope.launch {
            try {
                val evento = eventoRepository.getEventoPorId(eventoId)
                if (evento != null) {
                    val eventoAtualizado = evento.copy(presencas = presencas)
                    eventoRepository.confirmarPresenca(eventoAtualizado)
                    recarregarEventos()
                }
            } catch (e: Exception) {
                // Log do erro
            }
        }
    }
}
