package br.com.shubudo.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.model.Evento
import br.com.shubudo.repositories.EventoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EventoDetalheUiState(
    val isLoading: Boolean = false,
    val evento: Evento? = null,
    val error: String? = null
)

@HiltViewModel
class EventoDetalheViewModel @Inject constructor(
    private val eventoRepository: EventoRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(EventoDetalheUiState())
    val uiState: StateFlow<EventoDetalheUiState> = _uiState.asStateFlow()
    
    fun loadEvento(eventoId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // Get all events and find the one with matching ID
                val eventos = eventoRepository.getEventos().first()
                val evento = eventos.find { it._id == eventoId }
                
                if (evento != null) {
                    _uiState.update { it.copy(isLoading = false, evento = evento, error = null) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Evento não encontrado") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}