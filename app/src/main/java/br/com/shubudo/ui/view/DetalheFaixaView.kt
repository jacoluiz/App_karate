package br.com.shubudo.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
                modifier = Modifier.fillMaxSize()
            ) {
                // Cabeçalho com fundo colorido atrás do texto
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        text = "Que tipo de conteúdo você gostaria de ver?",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                // Conteúdo principal: drop-down de seleção
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(28.dp)
                ) {
                    CardSelecaoTipoConteudo(
                        programacao = uiState.programacao,
                        onNavigateToDetalheMovimento = onNavigateToDetalheMovimento
                    )
                }
            }
        }

        DetalheFaixaUiState.Empty -> TODO()
    }
}
