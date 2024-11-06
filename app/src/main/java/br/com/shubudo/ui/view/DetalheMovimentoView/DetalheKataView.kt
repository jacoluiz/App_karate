package br.com.shubudo.ui.view.DetalheMovimentoView

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import br.com.shubudo.ui.components.PlayerDeVideo
import br.com.shubudo.ui.components.botaoVoltar
import br.com.shubudo.ui.components.itemDetalheMovimento
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.ui.viewModel.DetalheMovimentoViewModel
import br.com.shubudo.utils.toOrdinario
import br.com.shubudo.utils.toOrdinarioFeminino

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TelaKata(uiState: DetalheMovimentoUiState.Success, onBackNavigationClick: () -> Unit) {
    val kata = uiState.kata

    var indexKataExibido by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()

    // PagerState para controlar o pager
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        kata[indexKataExibido].movimentos.size
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        PlayerDeVideo(videos =kata[indexKataExibido].video, temposVideos =kata[indexKataExibido].temposVideos)

        EsqueletoTela {


            // Horizontal Pager para rolar os movimentos horizontalmente
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { pageIndex ->
                // Cada página representa um movimento diferente
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    item {
                        // Controles para alterar o kata
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            IconButton(onClick = {
                                if (indexKataExibido != 0) indexKataExibido-- else indexKataExibido =
                                    kata.size - 1
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowLeft,
                                    contentDescription = "Seta para voltar",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            AnimatedContent(
                                targetState = indexKataExibido,
                                modifier = Modifier.weight(2f),
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
                                    text = "${kata[index]?.ordem?.toOrdinarioFeminino()} forma",
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.inverseSurface
                                )
                            }
                            IconButton(onClick = {
                                if (indexKataExibido < kata.size - 1) indexKataExibido++ else indexKataExibido =
                                    0
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowRight,
                                    contentDescription = "Seta para avançar",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Exibição do conteúdo do movimento atual
                    item {
                        Text(
                            modifier = Modifier
                                .padding(top = 22.dp, start = 16.dp)
                                .fillMaxWidth(),
                            text = "${kata[indexKataExibido].movimentos[pageIndex].ordem.toOrdinario()} movimento",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            itemDetalheMovimento(
                                descricao = "Tipo",
                                valor = kata[indexKataExibido].movimentos[pageIndex].tipoMovimento,
                                icone = Icons.Default.Accessibility
                            )
                            itemDetalheMovimento(
                                descricao = "Base",
                                valor = kata[indexKataExibido].movimentos[pageIndex].base,
                                icone = Icons.Default.Accessibility
                            )
                        }
                        Text(
                            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                            text = kata[indexKataExibido].movimentos[pageIndex].nome,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                            text = kata[indexKataExibido].movimentos[pageIndex].descricao,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // Espaçamento final
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
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
