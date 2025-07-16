package br.com.shubudo.ui.view.detalheMovimentoView.projecao

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import br.com.shubudo.model.Projecao
import br.com.shubudo.ui.components.BotaoVoltar
import br.com.shubudo.ui.components.LocalVideoPlayer
import br.com.shubudo.ui.view.detalheMovimentoView.EsqueletoTela

@Composable
fun TelaDetalheProjecao(
    faixa: String,
    projecao: Projecao,
    onBackNavigationClick: () -> Unit
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val scrollState = rememberScrollState()
    var isPlaying by remember { mutableStateOf(false) }

    // Criando o ExoPlayer corretamente
    val exoPlayer = remember(context, projecao.video) {
        createExoPlayer(context, projecao.video)
    }

    EsqueletoTela(faixa = faixa) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                LocalVideoPlayer(
                    exoPlayer = exoPlayer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    useController = false,
                    videoPath = projecao.video
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {
                    exoPlayer.seekTo(0)
                    exoPlayer.play()
                    isPlaying = true
                }) {
                    Icon(imageVector = Icons.Default.Replay, contentDescription = "Reiniciar")
                }
                IconButton(onClick = {
                    if (isPlaying) {
                        exoPlayer.pause()
                    } else {
                        exoPlayer.play()
                    }
                    isPlaying = !isPlaying
                }) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pausar" else "Reproduzir"
                    )
                }
            }

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

/**
 * Função para criar o ExoPlayer corretamente
 */
fun createExoPlayer(context: Context, videoUrl: String): ExoPlayer {
    return ExoPlayer.Builder(context).build().apply {
        val mediaItem = MediaItem.fromUri(videoUrl)
        setMediaItem(mediaItem)
        prepare()
    }
}
