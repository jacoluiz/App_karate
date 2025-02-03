package br.com.shubudo.ui.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.shubudo.ui.components.CardSelecaoTipoConteudo
import br.com.shubudo.ui.components.LoadingOverlay
import br.com.shubudo.ui.uistate.DetalheFaixaUiState

@Composable
fun DetalheFaixaView(
    uiState: DetalheFaixaUiState,
    onNavigateToDetalheMovimento: (String, String) -> Unit
) {
    when (uiState) {
        is DetalheFaixaUiState.Loading -> {
            // Mostra o overlay de loading
            LoadingOverlay(isLoading = true) {}
        }

        is DetalheFaixaUiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 16.dp, 16.dp),
                    horizontalArrangement = Arrangement.Absolute.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Que tipo de conteúdo você gostaria de ver?",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(28.dp)
                ) {
                    Log.i("DetalheFaixaView", "programacao: ${uiState.programacao}")
                    CardSelecaoTipoConteudo(uiState.programacao, onNavigateToDetalheMovimento)
                }
            }
        }

        DetalheFaixaUiState.Empty -> TODO()
    }
}