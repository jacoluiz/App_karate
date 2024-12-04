package br.com.shubudo.ui.view.DetalheMovimentoView

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import br.com.shubudo.ui.components.LoadingOverlay
import br.com.shubudo.ui.components.LocalVideoPlayer
import br.com.shubudo.ui.components.botaoVoltar
import br.com.shubudo.ui.components.itemDetalheMovimento
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.ui.viewModel.KataViewModel
import br.com.shubudo.utils.toOrdinario
import br.com.shubudo.utils.toOrdinarioFeminino
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TelaKata(
    viewModel: KataViewModel,
    uiState: DetalheMovimentoUiState.Success,
    onBackNavigationClick: () -> Unit
) {
    val kata = uiState.kata
    val context = LocalContext.current // Contexto obtido corretamente no escopo do Compose
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        ExoPlayer.STATE_BUFFERING -> Log.i(
                            "KataViewModel",
                            "ExoPlayer está carregando o vídeo."
                        )

                        ExoPlayer.STATE_READY -> Log.i(
                            "KataViewModel",
                            "ExoPlayer está pronto para reproduzir o vídeo."
                        )

                        ExoPlayer.STATE_ENDED -> Log.i(
                            "KataViewModel",
                            "A reprodução do vídeo terminou."
                        )

                        ExoPlayer.STATE_IDLE -> Log.i(
                            "KataViewModel",
                            "ExoPlayer está no estado idle."
                        )
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    Log.e("KataViewModel", "Erro ao reproduzir o vídeo: ${error.message}")
                }
            })
        }
    }

    var indexKataExibido by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()

    // PagerState para controlar o pager
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        kata[indexKataExibido].movimentos.size
    }
    
    val coroutineScope = rememberCoroutineScope()

    if (!viewModel.videoCarregado.value) {
        LaunchedEffect(indexKataExibido) {
            kata.getOrNull(indexKataExibido)?.let { currentKata ->
                Log.i("TelaKata", "Carregando vídeos para kata: ${currentKata.ordem}")
                viewModel.loadVideos(currentKata, context, exoPlayer)
            }
        }
        LoadingOverlay(true) { }
    } else {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter) // Alinha o vídeo no topo
            ) {


                Box(modifier = Modifier.fillMaxWidth()) {
                    // Player de vídeo
                    if (viewModel.videoCarregado.value) {
                        LocalVideoPlayer(viewModel = viewModel, exoPlayer = exoPlayer)
                    } else {
                        LoadingOverlay(true) { }
                    }
                }
            }

            EsqueletoTela {
                // Cada página representa um movimento diferente
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    item {
                        // Controles
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(16.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Botão Play/Pause
                                IconButton(onClick = {
                                    if (viewModel.isPlaying.value) {
                                        viewModel.pause(exoPlayer)
                                    } else {
                                        viewModel.play(exoPlayer)
                                    }
                                }) {
                                    Icon(
                                        imageVector = if (viewModel.isPlaying.value) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = "Play/Pause",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                // Botão para alternar vídeos
                                var currentIndex by remember { mutableIntStateOf(0) } // Índice do vídeo atual
                                val videos = kata.firstOrNull()?.video ?: emptyList()

                                IconButton(onClick = {
                                    if (videos.isNotEmpty()) {
                                        // Incrementa o índice, retornando ao início se for o último vídeo
                                        currentIndex = (currentIndex + 1) % videos.size
                                        val nextVideo = videos[currentIndex]
                                        viewModel.changeVideo(nextVideo, exoPlayer)
                                        viewModel.pause(exoPlayer)
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.FlipCameraAndroid,
                                        contentDescription = "Alternar vídeo",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    "Pular para o movimento:",
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically),
                                )
                            }
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                // Botões para tempos específicos
                                viewModel.currentVideo.value?.let { video ->
                                    kata.firstOrNull()?.temposVideos?.find { it.descricao == video.orientacao }?.tempo?.forEachIndexed { index, time ->
                                        TextButton(onClick = {
                                            viewModel.seekTo(
                                                exoPlayer,
                                                time * 1000L
                                            )
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(index)
                                            }
                                        }) {
                                            Text(text = "${index + 1}º")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item {
                        // Controles para alterar o kata
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            IconButton(onClick = {
                                // Muda para o kata anterior
                                if (indexKataExibido > 0) {
                                    indexKataExibido--
                                } else {
                                    indexKataExibido = kata.size - 1
                                }
                                // Carrega o novo vídeo para o kata atualizado
                                kata.getOrNull(indexKataExibido)?.let { novoKata ->
                                    viewModel.loadVideos(novoKata, context, exoPlayer)
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                                    contentDescription = "Seta para voltar",
                                    tint = MaterialTheme.colorScheme.primary
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
                                    text = "${kata[index].ordem.toOrdinarioFeminino()} forma",
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.inverseSurface
                                )
                            }
                            IconButton(onClick = {
                                // Muda para o próximo kata
                                if (indexKataExibido < kata.size - 1) {
                                    indexKataExibido++
                                } else {
                                    indexKataExibido = 0
                                }

                                // Carrega o novo vídeo para o kata atualizado
                                kata.getOrNull(indexKataExibido)?.let { novoKata ->
                                    viewModel.loadVideos(novoKata, context, exoPlayer)
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                                    contentDescription = "Seta para avançar",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                    }

                    // Exibição do conteúdo do movimento atual
                    item {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { pageIndex ->
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {

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
                                    modifier = Modifier.padding(
                                        top = 16.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                    text = kata[indexKataExibido].movimentos[pageIndex].nome,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    modifier = Modifier.padding(
                                        top = 16.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                    text = kata[indexKataExibido].movimentos[pageIndex].descricao,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
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
