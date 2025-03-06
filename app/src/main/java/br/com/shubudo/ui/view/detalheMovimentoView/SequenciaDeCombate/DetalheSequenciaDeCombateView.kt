package br.com.shubudo.ui.view.detalheMovimentoView.SequenciaDeCombate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.shubudo.model.SequenciaDeCombate
import br.com.shubudo.ui.components.BotaoVoltar
import br.com.shubudo.ui.components.itemDetalheMovimento
import br.com.shubudo.ui.view.detalheMovimentoView.EsqueletoTela
import br.com.shubudo.utils.toOrdinario

@Composable
fun TelaDetalheSequenciaDeCombate(
    faixa: String,
    sequenciaDeCombate: SequenciaDeCombate,
    onBackNavigationClick: () -> Unit
) {
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        EsqueletoTela (
            faixa = faixa
        ){
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
            ) {
                // Movimentos
                item {
                    sequenciaDeCombate.movimentos.forEachIndexed { index, movimento ->
                        Text(
                            modifier = Modifier.padding(top = 22.dp, start = 16.dp),
                            text = "${(index + 1).toOrdinario()} movimento",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 16.dp, 16.dp, 0.dp),
                        ) {
                            movimento.tipoMovimento?.let {
                                itemDetalheMovimento(
                                    descricao = "Tipo",
                                    valor = it,
                                    icone = Icons.Default.Accessibility
                                )
                            }
                            itemDetalheMovimento(
                                descricao = "Base",
                                valor = movimento.base,
                                icone = Icons.Default.Accessibility
                            )
                        }
                        Text(
                            modifier = Modifier.padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp
                            ),
                            text = movimento.nome,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            modifier = Modifier.padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp
                            ),
                            text = movimento.descricao,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        if (movimento.observacao.isNotEmpty()) {
                            Text(
                                modifier = Modifier.padding(
                                    top = 16.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                                text = "Observações/Dicas",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleSmall,
                            )
                            movimento.observacao.forEach { observacao ->
                                Text(
                                    modifier = Modifier.padding(
                                        top = 16.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                    text = observacao,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }
                }

                // Adicionar espaçamento no final da lista
                item {
                    Spacer(modifier = Modifier.height(32.dp))
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