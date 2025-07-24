package br.com.shubudo.ui.viewModel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.repositories.EventoRepository
import br.com.shubudo.ui.uistate.CadastroEventoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

@HiltViewModel
class CadastroEventoViewModel @Inject constructor(
    private val eventoRepository: EventoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CadastroEventoUiState>(CadastroEventoUiState.Form())
    val uiState: StateFlow<CadastroEventoUiState> = _uiState.asStateFlow()

    private var eventoIdParaEdicao: String? = null

    fun carregarEvento(eventoId: String) {
        eventoIdParaEdicao = eventoId
        viewModelScope.launch {
            _uiState.value = CadastroEventoUiState.Loading
            try {
                val evento = eventoRepository.getEventoPorId(eventoId)
                if (evento != null) {
                    // Parse da data ISO para campos separados
                    val dateTime = LocalDateTime.parse(evento.dataInicio.substring(0, 19))
                    val data = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    val horario = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

                    _uiState.value = CadastroEventoUiState.Form(
                        titulo = evento.titulo,
                        descricao = evento.descricao,
                        local = evento.local,
                        data = TextFieldValue(data),
                        horario = TextFieldValue(horario)
                    )
                } else {

                }
            } catch (e: Exception) {

            }
        }
    }

    fun updateTitulo(titulo: String) {
        val currentState = _uiState.value
        if (currentState is CadastroEventoUiState.Form) {
            _uiState.value = currentState.copy(
                titulo = titulo,
                tituloError = null
            )
        }
    }

    fun updateDescricao(descricao: String) {
        val currentState = _uiState.value
        if (currentState is CadastroEventoUiState.Form) {
            _uiState.value = currentState.copy(descricao = descricao)
        }
    }

    fun updateLocal(local: String) {
        val currentState = _uiState.value
        if (currentState is CadastroEventoUiState.Form) {
            _uiState.value = currentState.copy(
                local = local,
                localError = null
            )
        }
    }

    fun updateData(data: TextFieldValue) {
        val currentState = _uiState.value
        if (currentState is CadastroEventoUiState.Form) {
            _uiState.value = currentState.copy(
                data = data,
                dataError = null
            )
        }
    }

    fun updateHorario(horario: TextFieldValue) {
        val currentState = _uiState.value
        if (currentState is CadastroEventoUiState.Form) {
            _uiState.value = currentState.copy(
                horario = horario,
                horarioError = null
            )
        }
    }


    fun salvarEvento(
        onSuccess: () -> Unit = {},
    ) {
        val currentState = _uiState.value
        if (currentState !is CadastroEventoUiState.Form) return

        // Validação
        var hasError = false
        var tituloError: String? = null
        var localError: String? = null
        var dataError: String? = null
        var horarioError: String? = null

        if (currentState.titulo.isBlank()) {
            tituloError = "Título é obrigatório"
            hasError = true
        }

        if (currentState.local.isBlank()) {
            localError = "Local é obrigatório"
            hasError = true
        }

        // Validar formato da data
        try {
            LocalDate.parse(currentState.data.text, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } catch (e: DateTimeParseException) {
            dataError = "Data deve estar no formato dd/MM/yyyy"
            hasError = true
        }

        // Validar formato do horário
        try {
            LocalTime.parse(currentState.horario.text, DateTimeFormatter.ofPattern("HH:mm"))
        } catch (e: DateTimeParseException) {
            horarioError = "Horário deve estar no formato HH:mm"
            hasError = true
        }

        if (hasError) {
            _uiState.value = currentState.copy(
                tituloError = tituloError,
                localError = localError,
                dataError = dataError,
                horarioError = horarioError
            )
            return
        }

        // Salvar evento
        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true)
            try {
                // Converter data e horário para ISO string
                val data = LocalDate.parse(currentState.data.text, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                val horario = LocalTime.parse(currentState.horario.text, DateTimeFormatter.ofPattern("HH:mm"))
                val dateTime = LocalDateTime.of(data, horario)
                val isoString = dateTime.atZone(ZoneId.systemDefault()).toInstant().toString()

                if (eventoIdParaEdicao != null) {
                    eventoRepository.editarEvento(
                        eventoIdParaEdicao!!,
                        currentState.titulo,
                        currentState.descricao,
                        isoString,
                        currentState.local
                    )
                } else {
                    eventoRepository.criarEvento(
                        currentState.titulo,
                        currentState.descricao,
                        isoString,
                        currentState.local
                    )

                }

               onSuccess()
            } catch (e: Exception) {
                _uiState.value = currentState.copy(isLoading = false)
            }
        }
    }
}