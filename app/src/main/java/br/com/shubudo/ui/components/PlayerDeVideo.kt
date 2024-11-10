package br.com.shubudo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import br.com.shubudo.model.TempoVideo
import br.com.shubudo.model.Video
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun PlayerDeVideo(
    videos: List<Video>,
    temposVideos: List<TempoVideo>
) {
    val context = LocalContext.current
    var currentVideoIndex by remember { mutableIntStateOf(0) }
    val currentVideo = videos[currentVideoIndex]
    var youTubePlayerInstance by remember { mutableStateOf<YouTubePlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { ctx ->
            YouTubePlayerView(ctx).apply {
                enableAutomaticInitialization = false

                // Configurações do IFramePlayerOptions para ocultar os controles do YouTube
                val options = IFramePlayerOptions.Builder()
                    .controls(0)       // Oculta todos os controles
                    .rel(0)            // Desativa vídeos relacionados
                    .ivLoadPolicy(3)   // Oculta anotações e informações do vídeo
                    .ccLoadPolicy(0)   // Desativa legendas automáticas
                    .build()

                initialize(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayerInstance = youTubePlayer
                        youTubePlayer.cueVideo(currentVideo.url, 0f) // Carrega o vídeo inicial
                    }

                    override fun onStateChange(
                        youTubePlayer: YouTubePlayer,
                        state: PlayerConstants.PlayerState
                    ) {
                        isPlaying = state == PlayerConstants.PlayerState.PLAYING
                    }
                }, options)
            }
        }
    )



    Box(modifier = Modifier.fillMaxWidth()) {
        // Botão de Play/Pause
        IconButton(
            onClick = {
                youTubePlayerInstance?.let {
                    if (isPlaying) it.pause() else it.play()
                }
            },
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Play/Pause"
            )
        }

        // Botão para alternar entre ângulos
        IconButton(
            onClick = {
                currentVideoIndex = (currentVideoIndex + 1) % videos.size
                youTubePlayerInstance?.cueVideo(videos[currentVideoIndex].url, 0f)
            },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.primary,
                imageVector = Icons.AutoMirrored.Filled.RotateRight,
                contentDescription = "Mudar Ângulo"
            )
        }

        // Botões de salto para tempos específicos
        val currentTempos =
            temposVideos.find { it.descricao == currentVideo.orientacao }?.tempo ?: emptyList()
        Row(
            modifier = Modifier
                .padding(top = 50.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            currentTempos.forEachIndexed { index, time ->
                TextButton(
                    onClick = {
                        youTubePlayerInstance?.seekTo(time.toFloat()) // Salta para o tempo especificado
                    }
                ) {
                    Text(text = "${index + 1}")
                }
            }
        }
    }
}
