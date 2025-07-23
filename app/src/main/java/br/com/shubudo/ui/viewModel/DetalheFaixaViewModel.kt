package br.com.shubudo.ui.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.navigation.detalheFaixaArgument
import br.com.shubudo.repositories.ProgramacaoRepository
import br.com.shubudo.ui.uistate.DetalheFaixaUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalheFaixaViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: ProgramacaoRepository
) : ViewModel() {
    private var currentUiStateJob: Job? = null
    private val _uiState = MutableStateFlow<DetalheFaixaUiState>(
        DetalheFaixaUiState.Loading
    )

    val uiState = _uiState.asStateFlow()

    init {
        loadUiState()
    }

    private fun loadUiState() {
        currentUiStateJob?.cancel()
        currentUiStateJob = viewModelScope.launch {
            repository.findProgramacaoByCorFaixa(
                requireNotNull(savedStateHandle[detalheFaixaArgument])
            ).onStart {
                _uiState.update { DetalheFaixaUiState.Loading }
            }.collectLatest { programacao ->
                _uiState.update {
                    DetalheFaixaUiState.Success(programacao = programacao)
                }
            }
        }
    }
}
