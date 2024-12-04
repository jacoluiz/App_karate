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
import androidx.compose.material.icons.automirrored.filled._360
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
import br.com.shubudo.model.Orientacao
import br.com.shubudo.ui.components.LoadingOverlay
import br.com.shubudo.ui.components.LocalVideoPlayer
import br.com.shubudo.ui.components.botaoVoltar
import br.com.shubudo.ui.components.itemDetalheMovimento
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.ui.viewModel.KataViewModel
import br.com.shubudo.utils.toOrdinario
import br.com.shubudo.utils.toOrdinarioFeminino
import kotlinx.coroutines.delay
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
    val currentKata = kata.getOrNull(indexKataExibido)

// Atualizando o conteúdo de temposVideos com base no kata atual.
    val currentTemposVideos = currentKata?.temposVideos?.find { it.descricao == viewModel.currentVideo.value?.orientacao }

    val listState = rememberLazyListState()

    // PagerState para controlar o pager
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        kata[indexKataExibido].movimentos.size
    }

    val coroutineScope = rememberCoroutineScope()

    // Lógica para mover a página no início
    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            // Aguarda um curto período para garantir que a tela esteja carregada
            delay(500)

            // Move para a metade do próximo movimento (página 1)
            pagerState.animateScrollToPage(1, 0.5f)

            // Aguarda um pouco para o usuário ver que é um scroll e retorna para o primeiro movimento
            delay(500)

            // Volta para o primeiro movimento (página 0)
            pagerState.animateScrollToPage(0)
        }
    }

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
                                        // Obtém a orientação atual
                                        val currentVideo = viewModel.currentVideo.value
                                        val currentOrientation = currentVideo?.orientacao

                                        // Determina a próxima orientação de forma cíclica
                                        val nextOrientation = when (currentOrientation) {
                                            Orientacao.FRENTE -> Orientacao.ESQUERDA
                                            Orientacao.ESQUERDA -> Orientacao.DIREITA
                                            Orientacao.DIREITA -> Orientacao.COSTAS
                                            Orientacao.COSTAS -> Orientacao.FRENTE
                                            else -> Orientacao.FRENTE // Caso não tenha vídeo atual, começamos com FRENTE
                                        }

                                        // Encontra o vídeo correspondente à próxima orientação
                                        val nextVideo =
                                            videos.find { it.orientacao == nextOrientation }
                                        if (nextVideo != null) {
                                            // Altera para o próximo vídeo (próxima orientação)
                                            viewModel.changeVideo(nextVideo, exoPlayer)
                                            viewModel.pause(exoPlayer) // Opcionalmente, pausa o vídeo logo após a mudança
                                        }
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
                                currentTemposVideos?.tempo?.forEachIndexed { index, time ->
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
                    item {
                        // Controles para alterar o kata
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                                .padding(start = 16.dp)

                        ) {
                            AnimatedContent(
                                targetState = indexKataExibido,

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
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(onClick = {
                                // Avança para o próximo kata, ou volta ao início se já estiver no último
                                indexKataExibido = (indexKataExibido + 1) % kata.size

                                // Altera para o primeiro vídeo do novo kata, na orientação "FRENTE"
                                kata.getOrNull(indexKataExibido)?.let { novoKata ->
                                    Log.i("TelaKata", "Mudando para o kata: ${novoKata.ordem}")
                                    viewModel.changeKata(novoKata, Orientacao.FRENTE, context, exoPlayer)
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled._360,
                                    contentDescription = "Alternar kata",
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
                                    style = MaterialTheme.typography.titleMedium
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
