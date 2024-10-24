package br.com.shubudo.ui.view.DetalheMovimentoView

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState

@Composable
fun DetalheMovimentoView(
    uiState: DetalheMovimentoUiState, onBackNavigationClick: () -> Unit = {}
) {

    when (uiState) {
        is DetalheMovimentoUiState.Loading -> {
            Text("Loading...")
        }

        is DetalheMovimentoUiState.Success -> {
            if (uiState.movimento.isNotEmpty()) {
                telaMovimentoPadrao(uiState, onBackNavigationClick)
            } else if (uiState.defesaPessoal.isNotEmpty()) {
                telaDefesaPessoal(uiState, onBackNavigationClick)
            } else if (uiState.kata.isNotEmpty()) {
                telaKata(uiState, onBackNavigationClick)
            } else if (uiState.sequenciaDeCombate.isNotEmpty()) {
                telaSequenciaDeCombate(uiState, onBackNavigationClick)
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
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
        ) {
            content()
        }
    }
}