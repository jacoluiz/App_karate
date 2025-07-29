package br.com.shubudo.ui.view.recursos.programacao.detalheMovimento

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.shubudo.ui.components.LoadingWrapper
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.ui.view.recursos.programacao.detalheMovimento.MovimentosPadrao.TelaMovimentoPadraoManager
import br.com.shubudo.ui.view.recursos.programacao.detalheMovimento.SequenciaDeCombate.TelaSequenciaDeCombateManager
import br.com.shubudo.ui.view.recursos.programacao.detalheMovimento.SequenciaExtraBanner.TelaExtraBannerManager
import br.com.shubudo.ui.view.recursos.programacao.detalheMovimento.armamento.TelaArmamentoManager
import br.com.shubudo.ui.view.recursos.programacao.detalheMovimento.defesaPessoal.TelaDefesaPessoalManager
import br.com.shubudo.ui.view.recursos.programacao.detalheMovimento.defesasDeArmas.TelaDefesaDeArmaManager
import br.com.shubudo.ui.view.recursos.programacao.detalheMovimento.kata.TelaKataManager
import br.com.shubudo.ui.view.recursos.programacao.detalheMovimento.projecao.TelaProjecaoManager
import br.com.shubudo.ui.view.recursos.programacao.detalheMovimento.tecnicasDeChao.TelaTecnicasDeChaoManager
import br.com.shubudo.ui.viewModel.DetalheArmamentoViewModel
import br.com.shubudo.ui.viewModel.KataViewModel
import br.com.shubudo.ui.viewModel.TecnicaChaoViewModel

@Composable
fun DetalheMovimentoView(
    uiState: DetalheMovimentoUiState, onBackNavigationClick: () -> Unit = {}
) {

    when (uiState) {
        is DetalheMovimentoUiState.Loading -> {
            LoadingWrapper(
                isLoading = true,
                loadingText = "Carregando avisos do karate..."
            ) {}
        }

        is DetalheMovimentoUiState.Success -> {
            if (uiState.movimento.isNotEmpty()) {
                TelaMovimentoPadraoManager(uiState, onBackNavigationClick)
            } else if (uiState.defesaPessoal.isNotEmpty()) {
                TelaDefesaPessoalManager(uiState, onBackNavigationClick)
            } else if (uiState.kata.isNotEmpty()) {
                val viewModel: KataViewModel = viewModel()
                TelaKataManager(viewModel, uiState, onBackNavigationClick)
            } else if (uiState.sequenciaDeCombate.isNotEmpty()) {
                TelaSequenciaDeCombateManager(uiState, onBackNavigationClick)
            } else if (uiState.projecao.isNotEmpty()) {
                TelaProjecaoManager(uiState, onBackNavigationClick)
            } else if (uiState.sequenciaExtraBanner.isNotEmpty()) {
                TelaExtraBannerManager(uiState, onBackNavigationClick)
            } else if (uiState.armamento.isNotEmpty()) {
                val viewModel: DetalheArmamentoViewModel = viewModel()
                TelaArmamentoManager(uiState, viewModel, onBackNavigationClick)
            } else if (uiState.defesasDeArma.isNotEmpty()) {
                val viewModel: DetalheArmamentoViewModel = viewModel()
                TelaDefesaDeArmaManager(uiState, viewModel, onBackNavigationClick)
            } else if (uiState.tecnicasDeChao.isNotEmpty()) {
                val viewModel: TecnicaChaoViewModel = viewModel()
                TelaTecnicasDeChaoManager(uiState, viewModel, onBackNavigationClick)
            }
        }

        DetalheMovimentoUiState.Empty -> Box {
            Text("Opa, isso é um erro, seria bom reportalo :)")
        }
    }
}