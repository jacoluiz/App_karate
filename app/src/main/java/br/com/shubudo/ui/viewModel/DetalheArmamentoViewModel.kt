package br.com.shubudo.ui.viewModel

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import br.com.shubudo.model.Armamento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DetalheArmamentoViewModel : ViewModel() {

    var isPlaying = mutableStateOf(false)
        private set

    var currentVideoPath = mutableStateOf<String?>(null)
        private set

    var videoCarregado = mutableStateOf(false)
        private set

    private var isLoadingVideo = false

    @OptIn(UnstableApi::class)
    fun loadVideo(armamento: Armamento, context: Context, exoPlayer: ExoPlayer) {
        if (isLoadingVideo) return
        isLoadingVideo = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                videoCarregado.value = false

                // Baixa o vídeo do armamento e salva localmente.
                // Considera que armamento.video contém a URL do vídeo e armamento.id identifica o armamento.
                val localPath = downloadFileToExternal(context, armamento.video, armamento._id)
                currentVideoPath.value = localPath

                if (File(localPath).exists()) {
                    withContext(Dispatchers.Main) {
                        try {
                            // Libera quaisquer itens anteriores e configura o player com o novo vídeo.
                            exoPlayer.stop()
                            exoPlayer.clearMediaItems()

                            val mediaItem = MediaItem.fromUri(localPath)
                            exoPlayer.setMediaItem(mediaItem)
                            exoPlayer.prepare()
                            exoPlayer.playWhenReady = false

                            videoCarregado.value = true
                        } catch (e: Exception) {
                            Log.e("ArmamentoViewModel", "Erro ao configurar o player: ${e.message}")
                        }
                    }
                } else {
                    Log.e("ArmamentoViewModel", "O arquivo não existe: $localPath")
                }
            } finally {
                isLoadingVideo = false
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

    private fun downloadFileToExternal(
        context: Context,
        url: String,
        armamentoId: String
    ): String {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = "$armamentoId.mp4"
        val file = File(downloadsDir, fileName)

        if (!file.exists()) {
            try {
                val urlObject = URL(url)
                val connection = urlObject.openConnection() as HttpURLConnection
                connection.connect()

                // Verifica se o download foi bem-sucedido.
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
                Log.e("ArmamentoViewModel", "Erro ao baixar o vídeo: ${e.message}")
            }
        }
        return file.absolutePath
    }
}
