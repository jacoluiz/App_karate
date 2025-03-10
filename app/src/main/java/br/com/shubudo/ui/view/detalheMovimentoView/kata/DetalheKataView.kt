package br.com.shubudo.ui.view.detalheMovimentoView.kata

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import br.com.shubudo.model.Kata
import br.com.shubudo.model.Orientacao
import br.com.shubudo.ui.components.BotaoVoltar
import br.com.shubudo.ui.components.LoadingOverlay
import br.com.shubudo.ui.components.LocalVideoPlayer
import br.com.shubudo.ui.components.itemDetalheMovimento
import br.com.shubudo.ui.view.detalheMovimentoView.EsqueletoTela
import br.com.shubudo.ui.viewModel.KataViewModel
import br.com.shubudo.utils.toOrdinario
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TelaDetalheKata(
    faixa: String,
    viewModel: KataViewModel,
    kata: Kata,
    onBackNavigationClick: () -> Unit
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Estado do pager (uma página para cada movimento)
    val pagerState = rememberPagerState(initialPage = 0) { kata.movimentos.size }

    // Instância do ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        ExoPlayer.STATE_BUFFERING ->
                            Log.i("KataViewModel", "ExoPlayer está carregando o vídeo.")
                        ExoPlayer.STATE_READY ->
                            Log.i("KataViewModel", "ExoPlayer está pronto para reproduzir o vídeo.")
                        ExoPlayer.STATE_ENDED ->
                            Log.i("KataViewModel", "A reprodução do vídeo terminou.")
                        ExoPlayer.STATE_IDLE ->
                            Log.i("KataViewModel", "ExoPlayer está no estado idle.")
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    Log.e("KataViewModel", "Erro ao reproduzir o vídeo: ${error.message}")
                }
            })
        }
    }

    // Liberando o ExoPlayer quando o Composable sair de cena (boa prática)
    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    // Carrega os vídeos sempre que o ViewModel ou o Kata mudarem (ou somente no início)
    LaunchedEffect(viewModel, kata) {
        viewModel.loadVideos(kata, context, exoPlayer)
    }

    // Extrai o caminho do vídeo atual a partir do viewModel
    val videoPath = viewModel.localFilePaths.value[viewModel.currentVideo.value?.orientacao]

    if (!viewModel.videoCarregado.value) {
        // Exibe um overlay de carregamento enquanto o vídeo não estiver pronto
        LoadingOverlay(true) { }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            // Player de vídeo na parte superior da tela
            LocalVideoPlayer(
                videoPath = videoPath,
                exoPlayer = exoPlayer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                useController = false
            )
            EsqueletoTela(faixa = faixa) {
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    item {
                        // Seção superior com botões de controle e seleção de tempos
                        Column(modifier = Modifier.fillMaxWidth()) {
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
                                        imageVector = if (viewModel.isPlaying.value) {
                                            Icons.Default.Pause
                                        } else {
                                            Icons.Default.PlayArrow
                                        },
                                        contentDescription = "Play/Pause",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                // Botão para alternar a orientação do vídeo
                                IconButton(onClick = {
                                    if (kata.video.isNotEmpty()) {
                                        val currentVideo = viewModel.currentVideo.value
                                        val currentOrientation = currentVideo?.orientacao

                                        // Define a próxima orientação de forma cíclica
                                        val nextOrientation = when (currentOrientation) {
                                            Orientacao.FRENTE -> Orientacao.ESQUERDA
                                            Orientacao.ESQUERDA -> Orientacao.DIREITA
                                            Orientacao.DIREITA -> Orientacao.COSTAS
                                            Orientacao.COSTAS -> Orientacao.FRENTE
                                            else -> Orientacao.FRENTE
                                        }

                                        // Encontra o vídeo correspondente à próxima orientação
                                        val nextVideo = kata.video.find {
                                            it.orientacao == nextOrientation
                                        }
                                        if (nextVideo != null) {
                                            viewModel.changeVideo(nextVideo, exoPlayer)
                                            // Opcionalmente, pausa o vídeo após a mudança
                                            viewModel.pause(exoPlayer)
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
                            // Título centralizado para a lista de tempos
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Pular para o movimento:",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                            // Botões que pulam para os tempos específicos no vídeo
                            val currentTemposVideos =
                                kata.temposVideos.find { it.descricao == viewModel.currentVideo.value?.orientacao }
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                currentTemposVideos?.tempo?.forEachIndexed { index, time ->
                                    TextButton(onClick = {
                                        viewModel.seekTo(exoPlayer, time * 1000L)
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
                    // HorizontalPager exibindo cada movimento
                    item {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { pageIndex ->
                            Column(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    modifier = Modifier
                                        .padding(top = 22.dp, start = 16.dp)
                                        .fillMaxWidth(),
                                    text = "${kata.movimentos[pageIndex].ordem.toOrdinario()} movimento",
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
                                    kata.movimentos[pageIndex].tipoMovimento?.let {
                                        itemDetalheMovimento(
                                            descricao = "Tipo",
                                            valor = it,
                                            icone = Icons.Default.Accessibility
                                        )
                                    }
                                    itemDetalheMovimento(
                                        descricao = "Base",
                                        valor = kata.movimentos[pageIndex].base,
                                        icone = Icons.Default.Accessibility
                                    )
                                }
                                Text(
                                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                                    text = kata.movimentos[pageIndex].nome,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                                    text = kata.movimentos[pageIndex].descricao,
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

    // Botão de voltar
    BotaoVoltar(
        listState = listState,
        onBackNavigationClick = onBackNavigationClick
    )
}
