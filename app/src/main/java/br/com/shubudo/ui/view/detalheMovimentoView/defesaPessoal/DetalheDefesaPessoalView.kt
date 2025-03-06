package br.com.shubudo.ui.view.detalheMovimentoView.defesaPessoal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.shubudo.model.DefesaPessoal
import br.com.shubudo.ui.components.BotaoVoltar
import br.com.shubudo.ui.view.detalheMovimentoView.EsqueletoTela

@Composable
fun TelaDetalheDefesaPessoal(
    faixa: String,
    defesaPessoal: DefesaPessoal,
    onBackNavigationClick: () -> Unit
) {
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        EsqueletoTela(
            faixa = faixa
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
            ) {
                items(defesaPessoal.movimentos) { movimento ->
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = movimento.nome,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        modifier = Modifier.padding(
                            top = 0.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ),
                        text = movimento.descricao,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

            }
        }
        // Botão de voltar flutuante
        BotaoVoltar(
            listState = listState,
            onBackNavigationClick = onBackNavigationClick
        )
    }
}
