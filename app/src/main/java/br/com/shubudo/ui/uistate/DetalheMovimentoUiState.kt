package br.com.shubudo.ui.uistate

import br.com.shubudo.model.DefesaPessoal
import br.com.shubudo.model.Kata
import br.com.shubudo.model.Movimento
import br.com.shubudo.model.SequenciaDeCombate

sealed class DetalheMovimentoUiState {

    object Loading : DetalheMovimentoUiState()

    data class Success(
        val movimento: List<Movimento>,
        val defesaPessoal: List<DefesaPessoal>,
        val kata: List<Kata>,
        val sequenciaDeCombate: List<SequenciaDeCombate>,
        val faixa: String,
    ) : DetalheMovimentoUiState()

    object Empty : DetalheMovimentoUiState()
}