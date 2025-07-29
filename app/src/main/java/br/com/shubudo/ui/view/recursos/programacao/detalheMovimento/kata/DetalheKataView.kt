package br.com.shubudo.ui.view.recursos.programacao.detalheMovimento.kata

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import br.com.shubudo.model.Kata
import br.com.shubudo.model.Orientacao
import br.com.shubudo.ui.components.LoadingOverlay
import br.com.shubudo.ui.components.OnlineVideoPlayer
import br.com.shubudo.ui.viewModel.KataViewModel
import kotlinx.coroutines.launch

@Composable
fun TelaDetalheKata(
    viewModel: KataViewModel,
    kata: Kata,
    onBackNavigationClick: () -> Unit
) {
    val context = LocalContext.current
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

    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    LaunchedEffect(viewModel, kata) {
        viewModel.loadVideos(kata, exoPlayer)
    }

    val videoUrl = viewModel.currentVideo.value?.url

    var currentMovementIndex by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { kata.movimentos.size }
    )

    LaunchedEffect(currentMovementIndex) {
        coroutineScope.launch {
            if (currentMovementIndex != pagerState.currentPage) {
                pagerState.animateScrollToPage(currentMovementIndex)
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (currentMovementIndex != pagerState.currentPage) {
            currentMovementIndex = pagerState.currentPage
        }
    }

    if (!viewModel.videoCarregado.value) {
        LoadingOverlay(true) {}
    } else {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF202020)
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(Color(0xFF121212))
                ) {
                    OnlineVideoPlayer(
                        videoUrl = videoUrl,
                        exoPlayer = exoPlayer,
                        modifier = Modifier.fillMaxSize(),
                        useController = false
                    )

                    IconButton(
                        onClick = onBackNavigationClick,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                            .size(36.dp)
                            .background(Color(0x80000000), RoundedCornerShape(18.dp))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconButton(onClick = {
                                if (viewModel.isPlaying.value) viewModel.pause(exoPlayer)
                                else viewModel.play(exoPlayer)
                            }) {
                                Icon(
                                    imageVector = if (viewModel.isPlaying.value) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play/Pause",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            IconButton(onClick = {
                                viewModel.seekTo(exoPlayer, 0)
                                viewModel.play(exoPlayer)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Replay,
                                    contentDescription = "Reiniciar",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            IconButton(onClick = {
                                kata.video.find {
                                    it.orientacao == when (viewModel.currentVideo.value?.orientacao) {
                                        Orientacao.FRENTE -> Orientacao.ESQUERDA
                                        Orientacao.ESQUERDA -> Orientacao.DIREITA
                                        Orientacao.DIREITA -> Orientacao.COSTAS
                                        Orientacao.COSTAS -> Orientacao.FRENTE
                                        else -> Orientacao.FRENTE
                                    }
                                }?.let {
                                    viewModel.changeVideo(it, exoPlayer)
                                    viewModel.pause(exoPlayer)
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.FlipCameraAndroid,
                                    contentDescription = "Alternar ângulo",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Text(
                            text = "Pular para o movimento:",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, bottom = 8.dp),
                            textAlign = TextAlign.Center
                        )

                        val currentTemposVideos = kata.temposVideos.find {
                            it.descricao == viewModel.currentVideo.value?.orientacao
                        }
                        val tempos = currentTemposVideos?.tempo ?: emptyList()
                        val totalMovements = kata.movimentos.size
                        val rows = (totalMovements + 4) / 5

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            for (row in 0 until rows) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    for (col in 0 until 5) {
                                        val index = row * 5 + col
                                        if (index < totalMovements) {
                                            val tempoMs =
                                                tempos.getOrNull(index)?.times(1000L) ?: 0L
                                            TextButton(
                                                onClick = {
                                                    viewModel.seekTo(exoPlayer, tempoMs)
                                                    currentMovementIndex = index
                                                    coroutineScope.launch {
                                                        pagerState.scrollToPage(index)
                                                    }
                                                },
                                                modifier = Modifier.padding(horizontal = 2.dp)
                                            ) {
                                                Text(text = "${index + 1}º", color = Color.White)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (kata.movimentos.isNotEmpty() && currentMovementIndex < kata.movimentos.size) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxWidth()
                        ) { page ->
                            val movimento = kata.movimentos[page]
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "${page + 1}º movimento",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    movimento.tipoMovimento?.let {
                                        Column {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = "Tipo:",
                                                    color = Color.Gray,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Text(
                                                    text = "Base:",
                                                    color = Color.Gray,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    modifier = Modifier.weight(1f)
                                                )
                                            }
                                            Row {
                                                Text(
                                                    text = it,
                                                    color = Color.White,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    modifier = Modifier.weight(1f)
                                                )

                                                Text(
                                                    text = movimento.base,
                                                    color = Color.White,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    modifier = Modifier.weight(1f)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                            text = movimento.nome,
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Medium
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = movimento.descricao,
                                            color = Color.LightGray,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
