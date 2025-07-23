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

@Composable
fun OnlineVideoPlayer(
    videoUrl: String?, // apenas URLs HTTP/HTTPS
    exoPlayer: ExoPlayer,
    modifier: Modifier = Modifier,
    useController: Boolean = false
) {
    LaunchedEffect(videoUrl) {
        videoUrl?.let { url ->
            if (url.startsWith("http://") || url.startsWith("https://")) {
                Log.i("OnlineVideoPlayer", "Reproduzindo: $url")
                exoPlayer.setMediaItem(MediaItem.fromUri(url))
                exoPlayer.playWhenReady = true
                exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ONE
                exoPlayer.volume = 0f
                exoPlayer.prepare()
            } else {
                Log.e("OnlineVideoPlayer", "URL inválida: $url")
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
