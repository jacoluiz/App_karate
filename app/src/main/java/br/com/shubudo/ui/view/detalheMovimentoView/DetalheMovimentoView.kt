package br.com.shubudo.ui.view.detalheMovimentoView

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.shubudo.ui.components.LoadingOverlay
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.ui.view.detalheMovimentoView.MovimentosPadrao.TelaMovimentoPadraoManager
import br.com.shubudo.ui.view.detalheMovimentoView.SequenciaDeCombate.TelaSequenciaDeCombateManager
import br.com.shubudo.ui.view.detalheMovimentoView.SequenciaExtraBanner.TelaExtraBannerManager
import br.com.shubudo.ui.view.detalheMovimentoView.armamento.TelaArmamentoManager
import br.com.shubudo.ui.view.detalheMovimentoView.defesaPessoal.TelaDefesaPessoalManager
import br.com.shubudo.ui.view.detalheMovimentoView.defesasDeArmas.TelaDefesaDeArmaManager
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
            } else if (uiState.defesasDeArma.isNotEmpty()) {
                val viewModel: DetalheArmamentoViewModel = viewModel()
                TelaDefesaDeArmaManager(uiState, viewModel, onBackNavigationClick)
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
    
    // Determina as cores do gradiente baseado na faixa
    val gradientColors = when {
        faixa.trim().lowercase().startsWith("preta") -> listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
        else -> listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column {
                // Cabeçalho com gradiente
                Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = gradientColors
                        )
                    )
                )
                
                // Conteúdo principal
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-32).dp)
                ) {
                    content()
                }
            }
        }
    }
}
