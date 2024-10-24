package br.com.shubudo.ui.view.DetalheMovimentoView

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.List
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.R
import br.com.shubudo.ui.components.botaoVoltar
import br.com.shubudo.ui.components.itemDetalheMovimento
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.utils.toOrdinario
import br.com.shubudo.utils.toOrdinarioFeminino


@Composable
fun telaMovimentoPadrao(
    uiState: DetalheMovimentoUiState.Success,
    onBackNavigationClick: () -> Unit = {}
) {
    var movimento = uiState.movimento
    var indexMovimento by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        EsqueletoTela {
            AnimatedContent(
                targetState = movimento[indexMovimento],
                transitionSpec = {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(durationMillis = 600)
                    ) togetherWith slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(durationMillis = 600)
                    ) using SizeTransform(clip = false)
                },
                label = "Animação de Conteudo"
            ) { movimentoExibido ->
                LazyColumn(
                    state = listState,
                ) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                        ) {
                            IconButton(onClick = {
                                if (indexMovimento != 0) indexMovimento-- else indexMovimento =
                                    movimento.size - 1
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowLeft,
                                    contentDescription = "Seta para voltar",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            AnimatedContent(
                                targetState = indexMovimento,
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
                                    text = movimento[index].nome,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.inverseSurface,
                                    textAlign = TextAlign.Center,
                                )
                            }

                            IconButton(onClick = {
                                if (indexMovimento < movimento.size - 1) indexMovimento++ else indexMovimento =
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
                    item {
                        Text(
                            text = "Detalhes",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 16.dp, 16.dp, 16.dp),
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 0.dp, 16.dp, 0.dp),
                        ) {
                            movimentoExibido?.let {
                                itemDetalheMovimento(
                                    descricao = "Tipo",
                                    valor = it.tipoMovimento,
                                    icone = Icons.Default.Apps
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                itemDetalheMovimento(
                                    descricao = "Base",
                                    valor = it.base,
                                    icone = Icons.Default.Accessibility
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 0.dp, 16.dp, 52.dp),
                        ) {
                            movimentoExibido?.let {
                                itemDetalheMovimento(
                                    descricao = "Faixa",
                                    valor = uiState.faixa,
                                    iconPainter = painterResource(id = R.drawable.ic_faixa),

                                    )
                                Spacer(modifier = Modifier.width(8.dp))
                                itemDetalheMovimento(
                                    descricao = "Ordem",
                                    valor = if (it.tipoMovimento == "Defesa") it.ordem.toOrdinarioFeminino() else it.ordem.toOrdinario(),
                                    icone = Icons.Default.List
                                )
                            }
                        }
                        Text(
                            text = movimentoExibido?.descricao ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.inverseSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 0.dp, 16.dp, 8.dp),
                        )
                        if (movimentoExibido?.observacao?.isNotEmpty() == false) {
                            Text(
                                text = "Observações/Dicas",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 16.dp, 16.dp, 0.dp),
                            )
                            movimentoExibido?.observacao?.forEach { observacao ->
                                Text(
                                    text = observacao,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.inverseSurface,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp, 0.dp, 16.dp, 8.dp),
                                )
                            }
                        }
                    }
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