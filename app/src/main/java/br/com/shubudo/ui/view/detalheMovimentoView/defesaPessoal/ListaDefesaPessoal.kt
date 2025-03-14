package br.com.shubudo.ui.view.detalheMovimentoView.defesaPessoal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.shubudo.model.DefesaPessoal
import br.com.shubudo.ui.components.BotaoVoltar
import br.com.shubudo.ui.components.itemDetalheMovimento
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.utils.toOrdinarioFeminino

@Composable
fun TelaListaDefesaPessoal(
    uiState: DetalheMovimentoUiState.Success,
    onBackNavigationClick: () -> Unit = {},
    onCardClick: (defesaPesoal: DefesaPessoal) -> Unit // Callback para navegação ou troca de tela
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        item {
            Spacer(modifier = Modifier.padding(top = 46.dp))
        }
        // Lista de projeções
        items(uiState.defesaPessoal) { defesaPessoal ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .then(Modifier.padding(4.dp)), // Clique no Card
                elevation = CardDefaults.cardElevation(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                onClick = { onCardClick(defesaPessoal) } // Ao clicar no Card
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = defesaPessoal.numeroOrdem.toOrdinarioFeminino(),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    defesaPessoal.movimentos.forEach { movimento ->
                        Text(
                            text = movimento.nome,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                    }
                }
            }
        }
    }

// Botão de voltar flutuante
    BotaoVoltar(
        listState = listState,
        onBackNavigationClick = onBackNavigationClick
    )
}
