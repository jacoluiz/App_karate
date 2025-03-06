package br.com.shubudo.ui.view.detalheMovimentoView

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.shubudo.ui.components.LoadingOverlay
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.ui.view.detalheMovimentoView.MovimentosPadrao.TelaMovimentoPadraoManager
import br.com.shubudo.ui.view.detalheMovimentoView.SequenciaDeCombate.TelaSequenciaDeCombateManager
import br.com.shubudo.ui.view.detalheMovimentoView.SequenciaExtraBanner.TelaExtraBannerManager
import br.com.shubudo.ui.view.detalheMovimentoView.armamento.TelaArmamentoManager
import br.com.shubudo.ui.view.detalheMovimentoView.defesaPessoal.TelaDefesaPessoalManager
import br.com.shubudo.ui.view.detalheMovimentoView.kata.TelaKataManager
import br.com.shubudo.ui.view.detalheMovimentoView.projecao.TelaProjecaoManager
import br.com.shubudo.ui.viewModel.DetalheArmamentoViewModel
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
            } else if (uiState.armamento.isNotEmpty()) {
                val viewModel: DetalheArmamentoViewModel = viewModel()
                TelaArmamentoManager(uiState, viewModel, onBackNavigationClick)
            }
        }

        DetalheMovimentoUiState.Empty -> Box {
            Text("Opa, isso é um erro, seria bom reportalo :)")
        }
    }
}

@Composable
fun EsqueletoTela(
    faixa: String,
    content: @Composable () -> Unit
) {
    // Verifica se o tema é escuro
    val isDark = isSystemInDarkTheme()
    // Se a faixa começar com "preta", "preta 1 dan", etc., use surface no dark, caso contrário background
    val backgroundColor: Color = if (faixa.trim().lowercase().startsWith("preta")) {
        if (isDark) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background
    } else {
        MaterialTheme.colorScheme.background
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = backgroundColor,
            shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
        ) {
            content()
        }
    }
}
