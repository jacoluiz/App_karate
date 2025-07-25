package br.com.shubudo.ui.uistate

import br.com.shubudo.model.Armamento
import br.com.shubudo.model.DefesaPessoal
import br.com.shubudo.model.DefesaPessoalExtraBanner
import br.com.shubudo.model.Kata
import br.com.shubudo.model.Movimento
import br.com.shubudo.model.Projecao
import br.com.shubudo.model.SequenciaDeCombate
import br.com.shubudo.model.TecnicaChao

sealed class DetalheMovimentoUiState {

    object Loading : DetalheMovimentoUiState()

    data class Success(
        val movimento: List<Movimento>,
        val defesaPessoal: List<DefesaPessoal>,
        val kata: List<Kata>,
        val sequenciaDeCombate: List<SequenciaDeCombate>,
        val sequenciaExtraBanner: List<DefesaPessoalExtraBanner>,
        val projecao: List<Projecao>,
        val faixa: String,
        val armamento: List<Armamento>,
        val defesasDeArma: List<Armamento>,
        val tecnicasDeChao: List<TecnicaChao>

    ) : DetalheMovimentoUiState()

    object Empty : DetalheMovimentoUiState()
}