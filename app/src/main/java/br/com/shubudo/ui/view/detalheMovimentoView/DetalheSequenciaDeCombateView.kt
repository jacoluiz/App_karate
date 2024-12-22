package br.com.shubudo.ui.view.detalheMovimentoView

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.ui.components.botaoVoltar
import br.com.shubudo.ui.components.itemDetalheMovimento
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.utils.toOrdinario
import br.com.shubudo.utils.toOrdinarioFeminino

@Composable
fun telaSequenciaDeCombate(
    uiState: DetalheMovimentoUiState.Success,
    onBackNavigationClick: () -> Unit
) {
    val sequenciaDeCombate = uiState.sequenciaDeCombate
    var indexSequencia by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        EsqueletoTela {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
            ) {
                // Cabeçalho ou controles acima da lista
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(top = 16.dp),
                    ) {
                        if (sequenciaDeCombate.size > 1) {
                            IconButton(onClick = {
                                if (indexSequencia != 0) indexSequencia-- else indexSequencia =
                                    sequenciaDeCombate.size - 1
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowLeft,
                                    contentDescription = "Seta para voltar",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        AnimatedContent(
                            targetState = indexSequencia,
                            modifier = Modifier
                                .weight(2f),
                            transitionSpec = {
                                slideInHorizontally(
                                    initialOffsetX = { it },
                                    animationSpec = tween(durationMillis = 100)
                                ) togetherWith slideOutHorizontally(
                                    targetOffsetX = { -it },
                                    animationSpec = tween(durationMillis = 100)
                                ) using SizeTransform(clip = false)
                            },
                            label = ""
                        ) { index ->
                            Text(
                                text = "${sequenciaDeCombate[index]?.numeroOrdem?.toOrdinarioFeminino()} sequncia de combate",
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Center,
                            )
                        }
                        if (sequenciaDeCombate.size > 1) {

                            IconButton(onClick = {
                                if (indexSequencia < sequenciaDeCombate.size - 1) indexSequencia++ else indexSequencia =
                                    0
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowRight,
                                    contentDescription = "Seta para voltar",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f)

                                )
                            }
                        }
                    }
                }

                // Movimentos
                item {
                    sequenciaDeCombate[indexSequencia]?.movimentos?.forEachIndexed { index, movimento ->
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
                            itemDetalheMovimento(
                                descricao = "Tipo",
                                valor = movimento.tipoMovimento,
                                icone = Icons.Default.Accessibility
                            )
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
        botaoVoltar(
            listState = listState,
            onBackNavigationClick = onBackNavigationClick
        )
    }
}