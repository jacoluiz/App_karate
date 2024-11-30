package br.com.shubudo.ui.viewModel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import br.com.shubudo.model.Orientacao
import br.com.shubudo.model.Video
import br.com.shubudo.ui.components.downloadVideos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KataViewModel : ViewModel() {
    var isPlaying = mutableStateOf(false)
        private set

    var currentVideo = mutableStateOf<Video?>(null)
        private set

    var localFilePaths = mutableStateOf<Map<Orientacao, String?>>(emptyMap())
        private set

    fun loadVideos(videos: List<Video>, context: Context, exoPlayer: ExoPlayer) {
        viewModelScope.launch(Dispatchers.IO) {
            // Baixa os vídeos e armazena os caminhos locais
            val downloadedVideos = downloadVideos(context, videos)
            localFilePaths.value = downloadedVideos

            // Define o primeiro vídeo como padrão
            val firstVideo = videos.firstOrNull()
            currentVideo.value = firstVideo

            firstVideo?.let { video ->
                val path = downloadedVideos[video.orientacao]
                if (path != null) {
                    updatePlayer(exoPlayer, path) // Atualiza o player
                }
            }
        }
    }

    private suspend fun updatePlayer(exoPlayer: ExoPlayer, videoPath: String) {
        withContext(Dispatchers.Main) { // Garante que a operação com o player ocorre na thread principal
            exoPlayer.setMediaItem(MediaItem.fromUri(videoPath))
            exoPlayer.prepare()
            exoPlayer.playWhenReady = false
        }
    }


    fun play(exoPlayer: ExoPlayer) {
        exoPlayer.play()
        isPlaying.value = true
    }

    fun pause(exoPlayer: ExoPlayer) {
        exoPlayer.pause()
        isPlaying.value = false
    }

    fun seekTo(exoPlayer: ExoPlayer, timeMs: Long) {
        exoPlayer.seekTo(timeMs)
    }

    fun changeVideo(video: Video, exoPlayer: ExoPlayer) {
        val path = localFilePaths.value[video.orientacao]
        if (path != null) {
            viewModelScope.launch {
                updatePlayer(exoPlayer, path) // Reutiliza o método de atualização do player
                currentVideo.value = video
            }
        }
    }

}
