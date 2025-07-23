package br.com.shubudo.ui.viewModel

import android.os.Environment
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import br.com.shubudo.model.Kata
import br.com.shubudo.model.Orientacao
import br.com.shubudo.model.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class KataViewModel : ViewModel() {
    var isPlaying = mutableStateOf(true)
        private set

    var currentVideo = mutableStateOf<Video?>(null)
        private set

    var localFilePaths = mutableStateOf<Map<Orientacao, String>>(emptyMap())
        private set

    var videoCarregado = mutableStateOf(false)
        private set

    private var isLoadingVideos = false

    @OptIn(UnstableApi::class)
    fun loadVideos(kata: Kata, exoPlayer: ExoPlayer) {
        if (isLoadingVideos) return
        isLoadingVideos = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                videoCarregado.value = false

                val downloadedVideos: Map<Orientacao, String> = downloadVideos(
                    kata.video,
                    kata.ordem
                )
                localFilePaths.value = downloadedVideos


                kata.video.firstOrNull()?.let { video ->
                    val path: String? = downloadedVideos[video.orientacao]

                    if (path != null && File(path).exists()) {
                        withContext(Dispatchers.Main) {
                            try {
                                // Libere quaisquer itens anteriores no player e limpe
                                exoPlayer.stop()
                                exoPlayer.clearMediaItems()

                                // Crie o novo MediaItem e adicione ao ExoPlayer
                                val mediaItem = MediaItem.fromUri(path)
                                exoPlayer.setMediaItem(mediaItem)

                                // Prepare o player e configure para não iniciar automaticamente
                                exoPlayer.prepare()
                                exoPlayer.playWhenReady = false

                                // Atualiza o estado do vídeo carregado
                                videoCarregado.value = true
                                currentVideo.value = video
                            } catch (e: Exception) {
                                Log.e("KataViewModel", "Erro ao configurar o player: ${e.message}")
                            }
                        }
                    } else {
                        Log.e(
                            "KataViewModel",
                            "O path do vídeo é nulo ou o arquivo não existe: $path"
                        )
                    }
                }
            } finally {
                isLoadingVideos = false
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
        val path: String? = localFilePaths.value[video.orientacao]
        if (path != null) {
            viewModelScope.launch(Dispatchers.Main) {
                exoPlayer.stop()
                exoPlayer.setMediaItem(MediaItem.fromUri(path))
                exoPlayer.prepare()
                exoPlayer.playWhenReady = false
                currentVideo.value = video
            }
        }
    }

    private fun downloadVideos(
        videos: List<Video>,
        kataId: Int
    ): Map<Orientacao, String> {
        val downloadedPaths = mutableMapOf<Orientacao, String>()

        videos.forEach { video ->
            // Inclua o identificador do kata no nome do arquivo
            val localPath = downloadFileToExternal(
                video.url,
                kataId.toString(),
                video.orientacao.name
            )
            downloadedPaths[video.orientacao] = localPath
        }

        return downloadedPaths
    }

    private fun downloadFileToExternal(
        url: String,
        kataId: String,
        orientation: String
    ): String {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = "$kataId-$orientation.mp4"
        val file = File(downloadsDir, fileName)

        if (!file.exists()) {
            try {
                val urlObject = URL(url)
                val connection = urlObject.openConnection() as HttpURLConnection
                connection.connect()

                // Verifica o código HTTP de resposta
                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    throw Exception("Erro no download. Código HTTP: ${connection.responseCode}")
                }

                val input = BufferedInputStream(connection.inputStream)
                val output = FileOutputStream(file)

                val data = ByteArray(1024)
                var count: Int
                while (input.read(data).also { count = it } != -1) {
                    output.write(data, 0, count)
                }

                output.flush()
                output.close()
                input.close()

            } catch (e: Exception) {
                Log.e("KataViewModel", "Erro ao baixar o vídeo: ${e.message}")
            }
        }

        return file.absolutePath
    }
}
