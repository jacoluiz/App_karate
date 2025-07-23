package br.com.shubudo.ui.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import br.com.shubudo.model.Armamento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetalheArmamentoViewModel : ViewModel() {

    var isPlaying = mutableStateOf(false)
        private set

    var currentVideoUrl = mutableStateOf<String?>(null)
        private set

    var videoCarregado = mutableStateOf(false)
        private set

    private var isLoadingVideo = false

    fun loadVideo(armamento: Armamento, exoPlayer: ExoPlayer) {
        if (isLoadingVideo) return
        isLoadingVideo = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                videoCarregado.value = false

                val videoUrl = armamento.video
                currentVideoUrl.value = videoUrl

                withContext(Dispatchers.Main) {
                    try {
                        exoPlayer.stop()
                        exoPlayer.clearMediaItems()

                        val mediaItem = MediaItem.fromUri(videoUrl)
                        exoPlayer.setMediaItem(mediaItem)
                        exoPlayer.prepare()
                        exoPlayer.playWhenReady = false

                        videoCarregado.value = true
                    } catch (e: Exception) {
                        Log.e("ArmamentoViewModel", "Erro ao configurar o player: ${e.message}")
                    }
                }
            } finally {
                isLoadingVideo = false
            }
        }
    }


    fun setIsPlaying(value: Boolean) {
        isPlaying.value = value
    }
}
