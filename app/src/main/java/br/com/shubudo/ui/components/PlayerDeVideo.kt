package br.com.shubudo.ui.components

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import br.com.shubudo.model.Orientacao
import br.com.shubudo.model.Video
import br.com.shubudo.ui.viewModel.KataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

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

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
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


// Função para baixar os vídeos localmente
suspend fun downloadVideos(
    context: Context,
    videos: List<Video>
): Map<Orientacao, String?> = withContext(Dispatchers.IO) {
    val tempDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
    val downloadedVideos = mutableMapOf<Orientacao, String?>()

    videos.forEach { video ->
        try {
            val file = File(tempDir, "temp_video_${video.orientacao.name}.mp4")
            val connection = URL(video.url).openConnection()
            connection.connect()
            connection.getInputStream().use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            downloadedVideos[video.orientacao] = file.absolutePath
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "Erro ao baixar vídeo para ${video.orientacao}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            downloadedVideos[video.orientacao] = null
        }
    }

    downloadedVideos
}
