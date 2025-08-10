package br.com.shubudo.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager.idAcademiaVisualizacao
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
    private val _mostrarModalRelatorio = MutableStateFlow(false)
    val mostrarModalRelatorio: StateFlow<Boolean> = _mostrarModalRelatorio.asStateFlow()

    private val _eventosDisponiveis = MutableStateFlow<List<Evento>>(emptyList())
    val eventosDisponiveis: StateFlow<List<Evento>> = _eventosDisponiveis.asStateFlow()

    fun abrirModalRelatorioEvento(fluxo: String) {
        _mostrarModalRelatorio.value = true
        if (fluxo == "exame") {
            buscarEventosOficiais()
        } else {
            buscarEventosFuturos()
        }
    }

    fun fecharModalRelatorioEvento() {
        _mostrarModalRelatorio.value = false
    }

    /**
     * Atualiza do servidor e em seguida começa a observar os eventos futuros do banco local.
     */
    fun buscarEventosFuturos() {
        viewModelScope.launch {
            try {
                eventosRepository.refreshEventos()
                eventosRepository.getEventosFuturos().collect { lista ->
                    val filtrados = lista.filter { it.academia == idAcademiaVisualizacao }
                    _eventosDisponiveis.value = filtrados
                }
            } catch (_: Exception) {
                _eventosDisponiveis.value = emptyList()
            }
        }
    }

    fun buscarEventosOficiais() {
        viewModelScope.launch {
            try {
                eventosRepository.refreshEventos()
                eventosRepository.getEventosFuturos().collect { lista ->
                    val filtrados = lista.filter {
                        it.eventoOficial
                    }
                    _eventosDisponiveis.value = filtrados
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
        eventoId: String,
        conesMax: String? = null,
        filasMax: String? = null,
        fileName: String = "relatorio-organizado.xlsx"
    ) {
        _uiState.value = RelatorioUiState.Downloading
        viewModelScope.launch {
            try {
                val uri = repository.baixarESalvarRelatorioOrganizado(
                    context = context,
                    eventoId = eventoId,
                    conesMax = conesMax?.toInt(),
                    filasMax = filasMax,
                    fileName = fileName
                )
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
