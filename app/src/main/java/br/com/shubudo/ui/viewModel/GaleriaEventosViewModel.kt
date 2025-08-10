package br.com.shubudo.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager
import br.com.shubudo.SessionManager.idAcademiaVisualizacao
import br.com.shubudo.model.GaleriaEvento
import br.com.shubudo.network.services.GaleriaEventoRequest
import br.com.shubudo.repositories.GaleriaEventoRepository
import br.com.shubudo.repositories.GaleriaFotoRepository
import br.com.shubudo.ui.uistate.GaleriaEventosUiState
import br.com.shubudo.utils.formatarDataHoraLocal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GaleriaEventosViewModel @Inject constructor(
    private val eventoRepository: GaleriaEventoRepository,
    private val galeriaFotoRepository: GaleriaFotoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GaleriaEventosUiState>(GaleriaEventosUiState.Loading)
    val uiState: StateFlow<GaleriaEventosUiState> = _uiState.asStateFlow()

    private val _eventoSelecionado = MutableStateFlow<GaleriaEvento?>(null)
    val eventoSelecionado: StateFlow<GaleriaEvento?> = _eventoSelecionado.asStateFlow()

    var titulo by mutableStateOf("")
    var descricao by mutableStateOf("")
    var data by mutableStateOf("")

    init {
        refreshEventos()
    }

    fun refreshEventos() {
        viewModelScope.launch {
            _uiState.value = GaleriaEventosUiState.Loading
            try {
                eventoRepository.refreshEventos()
                eventoRepository.getEventos().collect { eventos ->
                    val eventosFiltrados =
                        eventos.filter { it.academiaId == idAcademiaVisualizacao }

                    _uiState.value = if (eventosFiltrados.isEmpty()) {
                        GaleriaEventosUiState.Empty
                    } else {
                        GaleriaEventosUiState.Success(eventosFiltrados)
                    }
                }

            } catch (e: Exception) {
                _uiState.value = GaleriaEventosUiState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun deletarEvento(eventoId: String) {
        viewModelScope.launch {
            try {
                eventoRepository.deletarEvento(eventoId)
                refreshEventos()
            } catch (e: Exception) {
                _uiState.update {
                    GaleriaEventosUiState.Error(e.message ?: "Erro ao deletar evento")
                }
            }
        }
    }

    fun carregarEvento(eventoId: String) {
        viewModelScope.launch {
            try {
                val eventos = eventoRepository.getEventos().first()
                val evento = eventos.find { it._id == eventoId }
                if (evento != null) {
                    titulo = evento.titulo
                    descricao = evento.descricao.toString()
                    data = formatarDataHoraLocal(evento.data, false)
                }
                _eventoSelecionado.value = evento
            } catch (e: Exception) {
                _eventoSelecionado.value = null
            }
        }
    }

    suspend fun numeroDeFotos(eventoId: String): Int {
        return try {
            galeriaFotoRepository.getFotosPorEvento(eventoId).first().size
        } catch (e: Exception) {
            0
        }
    }

    fun salvarEvento(
        id: String?,
        criadoPor: String,
        onErro: () -> Unit = {},
        onSalvo: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val entradaFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dataConvertida = try {
                    val localDate = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    val zonedDateTime = localDate.atStartOfDay(ZoneId.of("America/Sao_Paulo"))
                    val utcInstant = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toInstant()
                    DateTimeFormatter.ISO_INSTANT.format(utcInstant)
                } catch (e: Exception) {
                    _uiState.update {
                        GaleriaEventosUiState.Error("Data inválida. Use o formato dd/MM/yyyy")
                    }
                    return@launch
                }

                val filialId =
                    if (idAcademiaVisualizacao == SessionManager.usuarioLogado?.academiaId) {
                        SessionManager.usuarioLogado?.filialId
                    } else {
                        idAcademiaVisualizacao
                    } ?: ""

                val request = GaleriaEventoRequest(
                    titulo = titulo,
                    descricao = descricao,
                    data = dataConvertida,
                    academiaId = idAcademiaVisualizacao,
                    filialId = filialId,
                    criadoPor = criadoPor
                )

                if (id == null) {
                    eventoRepository.criarEvento(request)
                } else {
                    eventoRepository.editarEvento(id, request)
                }

                refreshEventos()
                onSalvo()

            } catch (e: Exception) {
                onErro()
                _uiState.update {
                    GaleriaEventosUiState.Error(e.message ?: "Erro ao salvar evento")
                }
            }
        }
    }
}
