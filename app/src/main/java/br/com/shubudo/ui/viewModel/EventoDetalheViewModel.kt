package br.com.shubudo.ui.viewModel

import EventoDetalheUiState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager.idAcademiaVisualizacao
import br.com.shubudo.SessionManager.usuarioLogado
import br.com.shubudo.model.Evento
import br.com.shubudo.model.Presenca
import br.com.shubudo.model.Usuario
import br.com.shubudo.repositories.EventoRepository
import br.com.shubudo.ui.uistate.CadastroAvisoUiState
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

    fun confirmarPresencasPorProfessor(
        evento: Evento,
        alunos: List<Usuario>
    ) {
        viewModelScope.launch {
            try {
                // índice rápido por email das presenças atuais
                val presencasPorEmail = evento.presencas.associateBy { it.email.lowercase() }.toMutableMap()

                // merge/update
                alunos.forEach { aluno ->
                    val email = aluno.email.lowercase()
                    val academiaDaPresenca = aluno.academiaId.ifBlank { idAcademiaVisualizacao }

                    val existente = presencasPorEmail[email]
                    if (existente != null) {
                        // já tinha — garante confirmado pelo professor = true
                        presencasPorEmail[email] = existente.copy(
                            confirmadoProfessor = true,
                            academia = existente.academia.ifBlank { academiaDaPresenca }
                        )
                    } else {
                        // adiciona novo
                        presencasPorEmail[email] = Presenca(
                            email = email,
                            confirmadoProfessor = true,
                            academia = academiaDaPresenca
                        )
                    }
                }

                val eventoAtualizado = evento.copy(
                    presencas = presencasPorEmail.values.toList()
                )

                val resposta = eventoRepository.confirmarPresenca(eventoAtualizado)
                _uiState.value = _uiState.value.copy(evento = resposta)
                eventoRepository.refreshEventos()
            } catch (e: Exception) {
                Log.e("EventoDetalheViewModel", "Erro ao confirmar presenças por professor", e)
            }
        }
    }

    fun confirmarOuCancelarPresenca(evento: Evento) {
        viewModelScope.launch {
            try {
                val emailUsuario = usuarioLogado?.email ?: return@launch
                val academiaAtual = idAcademiaVisualizacao

                // Já tem presença desse usuário?
                val jaConfirmado =
                    evento.presencas.any { it.email.equals(emailUsuario, ignoreCase = true) }

                val novasPresencas = if (jaConfirmado) {
                    // Cancela: remove a presença do usuário
                    evento.presencas.filterNot { it.email.equals(emailUsuario, ignoreCase = true) }
                } else {
                    // Confirma: adiciona nova presença
                    evento.presencas + Presenca(
                        email = emailUsuario,
                        confirmadoProfessor = false, // sempre false quando o aluno confirma
                        academia = academiaAtual
                    )
                }

                val eventoAtualizado = evento.copy(presencas = novasPresencas)

                val resposta = eventoRepository.confirmarPresenca(eventoAtualizado)
                _uiState.value = _uiState.value.copy(evento = resposta)

                // Atualiza cache/local
                eventoRepository.refreshEventos()
            } catch (e: Exception) {
                Log.e("EventoDetalheViewModel", "Erro ao confirmar/cancelar presença", e)
            }
        }
    }
}