package br.com.shubudo.ui.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import br.com.shubudo.model.TecnicaChao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TecnicaChaoViewModel @Inject constructor() : ViewModel() {

    val videoCarregado = mutableStateOf(false)
    val currentVideoUrl = mutableStateOf("")
    val isPlaying = mutableStateOf(false)

    fun loadVideo(tecnicaChao: TecnicaChao, exoPlayer: ExoPlayer) {
        viewModelScope.launch {
            try {
                videoCarregado.value = false
                currentVideoUrl.value = tecnicaChao.video

                // Simula carregamento do vídeo
                kotlinx.coroutines.delay(500)

                videoCarregado.value = true
            } catch (e: Exception) {
                videoCarregado.value = true
            }
        }
    }

    fun loadVideoFromUrl(videoUrl: String, exoPlayer: ExoPlayer) {
        viewModelScope.launch {
            try {
                videoCarregado.value = false
                currentVideoUrl.value = videoUrl

                // Simula carregamento do vídeo
                kotlinx.coroutines.delay(500)

                videoCarregado.value = true
            } catch (e: Exception) {
                videoCarregado.value = true
            }
        }
    }

    fun setIsPlaying(playing: Boolean) {
        isPlaying.value = playing
    }
}