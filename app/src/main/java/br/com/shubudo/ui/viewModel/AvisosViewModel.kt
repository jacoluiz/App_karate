package br.com.shubudo.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.repositories.AvisoRepository
import br.com.shubudo.ui.uistate.AvisoUiState
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
class AvisosViewModel @Inject constructor(
    private val avisoRepository: AvisoRepository
) : ViewModel() {

    private var currentUiStateJob: Job? = null
    private val _uiState = MutableStateFlow<AvisoUiState>(AvisoUiState.Loading)
    val avisoUiState: StateFlow<AvisoUiState> = _uiState.asStateFlow()

    init {
        refreshLocalAvisos()
    }

    private fun refreshLocalAvisos() {
        currentUiStateJob?.cancel()
        currentUiStateJob = viewModelScope.launch {
            // Atualiza os avisos locais antes de começar a coletar os dados
            avisoRepository.refreshAvisos()
            avisoRepository.getAvisos()
                .collectLatest { avisos ->
                    if (avisos.isEmpty()) {
                        _uiState.update { AvisoUiState.Empty }
                    } else {
                        _uiState.update { AvisoUiState.Success(avisos = avisos) }
                    }
                }
        }
    }

}
