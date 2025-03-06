package br.com.shubudo.ui.view.detalheMovimentoView.SequenciaExtraBanner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.shubudo.model.DefesaPessoalExtraBanner
import br.com.shubudo.model.Movimento
import br.com.shubudo.ui.components.BotaoVoltar
import br.com.shubudo.ui.view.detalheMovimentoView.EsqueletoTela
import br.com.shubudo.utils.toOrdinarioFeminino

@Composable
fun TelaDetalheExtraBanner(
    faixa: String,
    onBackNavigationClick: () -> Unit,
    extraBanner: DefesaPessoalExtraBanner
) {
    val listState = rememberLazyListState()

    EsqueletoTela(
        faixa = faixa
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${extraBanner.numeroOrdem.toOrdinarioFeminino()} defessa pessoal extra banner",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Lista de movimentos
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(extraBanner.movimentos) { movimento ->
                    MovimentoCard(movimento = movimento)
                }
            }

        }
    }
    BotaoVoltar(
        listState = listState,
        onBackNavigationClick = onBackNavigationClick
    )
}

@Composable
fun MovimentoCard(movimento: Movimento) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = movimento.nome,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Tipo: ${movimento.tipoMovimento}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = movimento.descricao,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (movimento.observacao.isNotEmpty()) {
                Text(
                    text = "Observações:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                movimento.observacao.forEach { obs ->
                    Text(
                        text = "- $obs",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
