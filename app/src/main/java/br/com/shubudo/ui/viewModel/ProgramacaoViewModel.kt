package br.com.shubudo.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.repositories.FaixaRepository
import br.com.shubudo.repositories.ProgramacaoRepository
import br.com.shubudo.ui.uistate.ProgramacaoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgramacaoViewModel @Inject constructor(
    private val repository: FaixaRepository,
    private val anotherRepository: ProgramacaoRepository
) : ViewModel() {
    private var currentUiStateJob: Job? = null
    private val _uiState = MutableStateFlow<ProgramacaoUiState>(
        ProgramacaoUiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadUiState()
    }

    private fun loadUiState() {
        currentUiStateJob?.cancel()
        currentUiStateJob = viewModelScope.launch {
            combine(
                repository.findAll(),
                anotherRepository.findProgramacaoByCorFaixa("Branca")
            ) { faixas, programacaoBranca ->
                faixas to programacaoBranca
            }.onStart {
                _uiState.update { ProgramacaoUiState.Loading }
            }.collectLatest { (faixas, programacaoBranca) ->
                if (false) {
                    _uiState.update {
                        ProgramacaoUiState.Empty
                    }
                } else {
                    _uiState.update {
                        ProgramacaoUiState.Success(
                            faixas = faixas.sortedBy { it.ordem },
                        )
                    }
                }
            }
        }
    }



}