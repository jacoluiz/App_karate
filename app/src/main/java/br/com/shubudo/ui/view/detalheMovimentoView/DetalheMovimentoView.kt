package br.com.shubudo.ui.view.detalheMovimentoView

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.shubudo.ui.components.LoadingOverlay
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.ui.view.detalheMovimentoView.MovimentosPadrao.TelaMovimentoPadraoManager
import br.com.shubudo.ui.view.detalheMovimentoView.SequenciaDeCombate.TelaSequenciaDeCombateManager
import br.com.shubudo.ui.view.detalheMovimentoView.SequenciaExtraBanner.TelaExtraBannerManager
import br.com.shubudo.ui.view.detalheMovimentoView.defesaPessoal.TelaDefesaPessoalManager
import br.com.shubudo.ui.view.detalheMovimentoView.kata.TelaKataManager
import br.com.shubudo.ui.view.detalheMovimentoView.projecao.TelaProjecaoManager
import br.com.shubudo.ui.viewModel.KataViewModel

@Composable
fun DetalheMovimentoView(
    uiState: DetalheMovimentoUiState, onBackNavigationClick: () -> Unit = {}
) {

    when (uiState) {
        is DetalheMovimentoUiState.Loading -> {
            LoadingOverlay(true) { }
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
            }
        }

        DetalheMovimentoUiState.Empty -> Box {
            Text("Opa, isso é um erro, seria bom reportalo :)")
        }
    }
}

@Composable
fun EsqueletoTela(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 250.dp),

            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
        ) {
            content()
        }
    }
}