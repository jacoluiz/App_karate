package br.com.shubudo.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                    if (eventos.isEmpty()) {
                        _eventosUiState.update { EventosUiState.Empty }
                    } else {
                        // Agrupa por data (yyyy-MM-dd)
                        val agrupadoPorData: Map<String, List<Evento>> = eventos.groupBy {
                            it.dataInicio.substring(0, 10)
                        }
                        _eventosUiState.update {
                            EventosUiState.Success(eventosAgrupados = agrupadoPorData)
                        }
                    }
                }
        }
    }
}
