package br.com.shubudo.ui.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.model.Armamento
import br.com.shubudo.model.DefesaPessoal
import br.com.shubudo.model.DefesaPessoalExtraBanner
import br.com.shubudo.model.Kata
import br.com.shubudo.model.Projecao
import br.com.shubudo.model.SequenciaDeCombate
import br.com.shubudo.model.TecnicaChao
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
                var defesaPessoal = emptyList<DefesaPessoal>()
                var sequenciaDeCombate = emptyList<SequenciaDeCombate>()
                var projecao = emptyList<Projecao>()
                var defesaExtraBanner = emptyList<DefesaPessoalExtraBanner>()
                var armamento = emptyList<Armamento>()
                var defesasDeArma = emptyList<Armamento>()
                var tecnicasDeChao = emptyList<TecnicaChao>()
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
                        sequenciaDeCombate = repository.findSequenciasDeCombateByFaixa(
                            idFaixa = repository.findFaixaByCor(faixa).first()._id
                        ).first()
                    }
                    "Projeções" -> {
                        projecao = repository.findProjecoesByFaixa(
                            idFaixa = repository.findFaixaByCor(faixa).first()._id
                        ).first()
                    }
                    "Defesas Extra Banner" -> {
                        defesaExtraBanner = repository.findDefesaPessoalExtraBannerByFaixa(
                            idFaixa = repository.findFaixaByCor(faixa).first()._id
                        ).first()
                    }
                    "Armamento" -> {
                        armamento = repository.findArmamentosByFaixa(
                            idFaixa = repository.findFaixaByCor(faixa).first()._id
                        ).first()
                    }
                    "Defesas de armas" -> {
                        defesasDeArma = repository.findDefesasDeArmasByFaixa(
                            idFaixa = repository.findFaixaByCor(faixa).first()._id
                        ).first()
                    }
                    "Técnicas de chão" -> {
                        tecnicasDeChao = repository.findTecnicasDeChaoByFaixa(
                            idFaixa = repository.findFaixaByCor(faixa).first()._id
                        ).first()
                    }
                }
                val movimentoList = repository.findMovimentoByFaixaETipo(faixa, movimento).first()

                _uiState.update {
                    DetalheMovimentoUiState.Success(
                        movimento = movimentoList.sortedBy { it.ordem },
                        faixa = faixa,
                        defesaPessoal = defesaPessoal.sortedBy { it.numeroOrdem },
                        kata = kata.sortedBy { it.ordem },
                        sequenciaDeCombate = sequenciaDeCombate.sortedBy { it.numeroOrdem },
                        projecao = projecao.sortedBy { it.ordem },
                        sequenciaExtraBanner = defesaExtraBanner.sortedBy { it.numeroOrdem },
                        armamento = armamento.sortedBy { it.numeroOrdem },
                        defesasDeArma = defesasDeArma.sortedBy { it.numeroOrdem },
                        tecnicasDeChao = tecnicasDeChao.sortedBy { it.ordem }
                    )
                }
            }
        }
    }
}
