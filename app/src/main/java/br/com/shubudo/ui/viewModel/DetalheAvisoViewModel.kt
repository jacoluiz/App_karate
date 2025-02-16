package br.com.shubudo.ui.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.navigation.detalheAvisoArgument
import br.com.shubudo.repositories.AvisoRepository
import br.com.shubudo.ui.uistate.DetalheAvisoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalheAvisoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: AvisoRepository
) : ViewModel() {

    private var currentUiStateJob: Job? = null
    private val _uiState = MutableStateFlow<DetalheAvisoUiState>(DetalheAvisoUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadUiState()
    }

    private fun loadUiState() {
        currentUiStateJob?.cancel()
        currentUiStateJob = viewModelScope.launch {
            val avisoId = requireNotNull(savedStateHandle[detalheAvisoArgument])
            flow {
                emit(repository.getAvisoById(avisoId.toString()))
            }
                .onStart {
                    _uiState.update { DetalheAvisoUiState.Loading }
                }
                .collectLatest { aviso ->
                    if (aviso != null) {
                        _uiState.update { DetalheAvisoUiState.Success(aviso = aviso) }
                    } else {
                        _uiState.update { DetalheAvisoUiState.Error("Aviso não encontrado") }
                    }
                }
        }
    }

    /**
     * Deleta o aviso atual.
     *
     * @param onSuccess Callback chamado se a exclusão for bem-sucedida.
     * @param onError Callback chamado com a mensagem de erro caso ocorra alguma falha.
     */
    fun deleteAviso(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val avisoId = requireNotNull(savedStateHandle[detalheAvisoArgument]).toString()
                val response = repository.excluirAviso(avisoId)
                // Se a resposta não for nula, consideramos que a exclusão foi bem-sucedida.
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erro desconhecido")
            }
        }
    }
}
