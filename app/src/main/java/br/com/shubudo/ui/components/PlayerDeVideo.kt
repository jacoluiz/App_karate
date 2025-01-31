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
import br.com.shubudo.ui.viewModel.KataViewModel
import java.io.File

@Composable
fun LocalVideoPlayer(viewModel: KataViewModel, exoPlayer: ExoPlayer) {
    val currentVideo = viewModel.currentVideo.value
    val localFilePaths = viewModel.localFilePaths.value

    LaunchedEffect(currentVideo) {
        currentVideo?.let { video ->
            localFilePaths[video.orientacao]?.let { path ->
                val file = File(path)
                if (file.exists()) {
                    exoPlayer.setMediaItem(MediaItem.fromUri(path))
                    exoPlayer.prepare()
                } else {
                    Log.e("Video", "Arquivo não encontrado: $path")
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                }
            }
        )
    }
}


