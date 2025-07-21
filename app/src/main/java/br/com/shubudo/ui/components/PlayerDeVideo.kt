package br.com.shubudo.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import java.io.File

@Composable
fun LocalVideoPlayer(
    videoPath: String?, // Caminho local ou URL do vídeo
    exoPlayer: ExoPlayer,
    modifier: Modifier = Modifier,
    useController: Boolean = false
) {
    LaunchedEffect(videoPath) {
        videoPath?.let { path ->
            // Verifica se é um arquivo local ou uma URL remota
            if (path.startsWith("http") || File(path).exists()) {
                exoPlayer.playWhenReady = true
                exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ONE
                exoPlayer.volume = 0f
                exoPlayer.setMediaItem(MediaItem.fromUri(path))
                exoPlayer.prepare()
            } else {
                Log.e("LocalVideoPlayer", "Arquivo não encontrado: $path")
            }
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    this.useController = useController
                }
            }
        )
    }
}

