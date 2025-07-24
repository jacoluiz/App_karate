package br.com.shubudo.ui.viewModel

import EventoDetalheUiState
import android.util.Log
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

@HiltViewModel
class EventoDetalheViewModel @Inject constructor(
    private val eventoRepository: EventoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventoDetalheUiState())
    val uiState: StateFlow<EventoDetalheUiState> = _uiState.asStateFlow()

    fun carregaEvento(eventoId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {

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

    fun confirmarOuCancelarPresenca(evento: Evento, emailUsuario: String) {
        viewModelScope.launch {
            try {
                val listaAtualizada = evento.confirmados.toMutableList()

                if (listaAtualizada.contains(emailUsuario)) {
                    listaAtualizada.remove(emailUsuario) // cancelar
                } else {
                    listaAtualizada.add(emailUsuario) // confirmar
                }

                val eventoAtualizado = evento.copy(confirmados = listaAtualizada)

                val resposta = eventoRepository.confirmarPresenca(eventoAtualizado)
                _uiState.value = _uiState.value.copy(evento = resposta)
                eventoRepository.refreshEventos()
            } catch (e: Exception) {
                Log.e("EventoDetalheViewModel", "Erro ao confirmar/cancelar presença", e)
            }
        }
    }

}