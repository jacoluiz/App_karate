package br.com.shubudo.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.model.Evento
import br.com.shubudo.repositories.EventoRepository
import br.com.shubudo.ui.uistate.EventoUiState
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
class EventosViewModel @Inject constructor(
    private val eventoRepository: EventoRepository
) : ViewModel() {

    private val _eventoUiState = MutableStateFlow<EventoUiState>(EventoUiState.Loading)
    val eventoUiState: StateFlow<EventoUiState> = _eventoUiState.asStateFlow()

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
                        _eventoUiState.update { EventoUiState.Empty }
                    } else {
                        // Agrupa por data (yyyy-MM-dd)
                        val agrupadoPorData: Map<String, List<Evento>> = eventos.groupBy {
                            it.dataInicio.substring(0, 10)
                        }
                        _eventoUiState.update {
                            EventoUiState.Success(eventosAgrupados = agrupadoPorData)
                        }
                    }
                }
        }
    }
}
