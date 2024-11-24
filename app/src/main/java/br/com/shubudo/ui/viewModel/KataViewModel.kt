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
            val downloadedVideos = downloadVideos(context, videos)
            localFilePaths.value = downloadedVideos

            // Define o primeiro vídeo como padrão e configura no player
            val firstVideo = videos.firstOrNull()
            currentVideo.value = firstVideo

            firstVideo?.let { video ->
                downloadedVideos[video.orientacao]?.let { path ->
                    withContext(Dispatchers.Main) { // Altere para o thread principal antes de interagir com o player
                        exoPlayer.setMediaItem(MediaItem.fromUri(path))
                        exoPlayer.prepare()
                    }
                }
            }
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
        currentVideo.value = video
        localFilePaths.value[video.orientacao]?.let { path ->
            exoPlayer.setMediaItem(MediaItem.fromUri(path))
            exoPlayer.prepare()
        }
    }
}
