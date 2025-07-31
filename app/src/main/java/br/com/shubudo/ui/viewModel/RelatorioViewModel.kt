package br.com.shubudo.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.model.Evento
import br.com.shubudo.repositories.EventoRepository
import br.com.shubudo.repositories.RelatorioRepository
import br.com.shubudo.ui.uistate.RelatorioUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RelatorioViewModel @Inject constructor(
    private val repository: RelatorioRepository,
    private val eventosRepository: EventoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RelatorioUiState>(RelatorioUiState.Idle)
    val uiState: StateFlow<RelatorioUiState> = _uiState.asStateFlow()

    // Modal de relatório por evento
    private val _mostrarModal = MutableStateFlow(false)
    val mostrarModal: StateFlow<Boolean> = _mostrarModal.asStateFlow()

    private val _eventosDisponiveis = MutableStateFlow<List<Evento>>(emptyList())
    val eventosDisponiveis: StateFlow<List<Evento>> = _eventosDisponiveis.asStateFlow()

    fun abrirModalRelatorioEvento() {
        _mostrarModal.value = true
        // Sempre que abrir a modal, atualiza a lista a partir do servidor:
        buscarEventosFuturos()
    }

    fun fecharModalRelatorioEvento() {
        _mostrarModal.value = false
    }

    /**
     * Atualiza do servidor e em seguida começa a observar os eventos futuros do banco local.
     */
    fun buscarEventosFuturos() {
        viewModelScope.launch {
            try {
                // 1) Faz refresh na API -> persiste no Room
                eventosRepository.refreshEventos()
                // 2) Coleta os eventos futuros do banco local (Flow)
                eventosRepository.getEventosFuturos().collect { lista ->
                    _eventosDisponiveis.value = lista
                }
            } catch (_: Exception) {
                _eventosDisponiveis.value = emptyList()
            }
        }
    }

    /**
     * BAIXA o relatório básico de organização (cones/filas).
     */
    fun baixarRelatorioOrganizado(
        context: Context,
        fileName: String = "relatorio-organizado.xlsx"
    ) {
        _uiState.value = RelatorioUiState.Downloading
        viewModelScope.launch {
            try {
                val uri = repository.baixarESalvarRelatorioOrganizado(context, fileName)
                _uiState.value = RelatorioUiState.Success(uri, fileName)
            } catch (e: Exception) {
                _uiState.value = RelatorioUiState.Error(e.message ?: "Erro ao baixar o relatório")
            }
        }
    }

    /**
     * Gera e baixa o relatório por evento (adultos/adolescentes ou primeira infância).
     */
    fun gerarRelatorioExamePorEvento(
        eventoId: String,
        isPrimeiraInfancia: Boolean,
        context: Context
    ) {
        _uiState.value = RelatorioUiState.Downloading
        viewModelScope.launch {
            try {
                val suffix = if (isPrimeiraInfancia) "primeira-infancia" else "exame"
                val fileName = "relatorio-$suffix-$eventoId.xlsx"
                val uri = repository.baixarESalvarRelatorioExamePorEvento(
                    context = context,
                    eventoId = eventoId,
                    primeiraInfancia = isPrimeiraInfancia,
                    fileName = fileName
                )
                _uiState.value = RelatorioUiState.Success(uri, fileName)
            } catch (e: Exception) {
                _uiState.value = RelatorioUiState.Error(
                    e.message ?: "Erro ao gerar relatório por evento"
                )
            }
        }
    }

    /** Volta para estado inicial (ex.: após Snackbar). */
    fun reset() {
        _uiState.value = RelatorioUiState.Idle
    }
}
