package br.com.shubudo.ui.view.detalheMovimentoView.projecao

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.shubudo.model.Projecao
import br.com.shubudo.ui.components.BotaoVoltar
import br.com.shubudo.ui.view.detalheMovimentoView.EsqueletoTela

@Composable
fun TelaDetalheProjecao(
    projecao: Projecao,
    onBackNavigationClick: () -> Unit
) {
    val listState = rememberLazyListState()

    EsqueletoTela {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

        ) {
            Text(
                text = projecao.nome,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = projecao.nomeJapones,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = projecao.descricao,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.padding(16.dp))
            projecao.observacao.forEach { obs ->
                Text(
                    text = "- $obs",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
    BotaoVoltar(
        listState = listState,
        onBackNavigationClick = onBackNavigationClick
    )

}
