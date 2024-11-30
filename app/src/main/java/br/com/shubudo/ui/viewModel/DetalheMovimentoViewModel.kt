package br.com.shubudo.ui.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.model.Kata
import br.com.shubudo.model.Orientacao
import br.com.shubudo.navigation.detalheFaixaArgument
import br.com.shubudo.navigation.detalheMovimentoArgument
import br.com.shubudo.repositories.ProgramacaoRepository
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalheMovimentoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: ProgramacaoRepository
) : ViewModel() {
    private var currentUiStateJob: Job? = null
    private val _uiState = MutableStateFlow<DetalheMovimentoUiState>(
        DetalheMovimentoUiState.Loading
    )

    val uiState = _uiState.asStateFlow()

    init {
        loadUiState()
    }

    private fun loadUiState() {
        val faixa = savedStateHandle.get<String>(detalheFaixaArgument)
        val movimento = savedStateHandle.get<String>(detalheMovimentoArgument)
        if (faixa != null && movimento != null) {
            currentUiStateJob?.cancel()
            currentUiStateJob = viewModelScope.launch {
                var kata = emptyList<Kata>()
                var defesaPessoal = emptyList<br.com.shubudo.model.DefesaPessoal>()
                var sequenciaDeCombate = emptyList<br.com.shubudo.model.SequenciaDeCombate>()
                when (movimento) {
                    "Katas" -> {
                        kata = repository.findKatasByFaixa(
                            idFaixa = repository.findFaixaByCor(faixa).first()._id
                        ).first()
                    }

                    "Defesa Pessoal" -> {
                        defesaPessoal = repository.findDefesasPessoaisByFaixa(
                            idFaixa = repository.findFaixaByCor(faixa).first()._id
                        ).first()
                    }

                    "Sequência de Combate" -> {
                        sequenciaDeCombate =
                            repository.findSequenciasDeCombateByFaixa(
                                idFaixa = repository.findFaixaByCor(
                                    faixa
                                ).first()._id
                            ).first()
                    }
                }
                val movimento = repository.findMovimentoByFaixaETipo(faixa, movimento).first()

                _uiState.update {

                    DetalheMovimentoUiState.Success(
                        movimento = movimento.sortedBy { it.ordem },
                        faixa = faixa,
                        defesaPessoal = defesaPessoal.sortedBy { it.numeroOrdem },
                        kata = kata.sortedBy { it.ordem },
                        sequenciaDeCombate = sequenciaDeCombate.sortedBy { it.numeroOrdem },

                    )
                }
            }
        }
    }
}
