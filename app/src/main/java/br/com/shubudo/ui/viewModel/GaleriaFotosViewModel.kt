package br.com.shubudo.ui.viewModel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.graphics.scale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager
import br.com.shubudo.model.GaleriaFoto
import br.com.shubudo.repositories.AcademiaRepository
import br.com.shubudo.repositories.GaleriaFotoRepository
import br.com.shubudo.ui.uistate.GaleriaFotosUiState
import br.com.shubudo.ui.uistate.UploadUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class GaleriaFotosViewModel @Inject constructor(
    private val galeriaRepository: GaleriaFotoRepository,
    private val academiaRepository: AcademiaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GaleriaFotosUiState>(GaleriaFotosUiState.Loading)
    val uiState: StateFlow<GaleriaFotosUiState> = _uiState.asStateFlow()

    private val _uploadUiState = MutableStateFlow<Map<String, UploadUiState>>(emptyMap())
    val uploadUiState: StateFlow<Map<String, UploadUiState>> = _uploadUiState.asStateFlow()

    suspend fun obterFilialIdDoUsuarioLogado(): String? {
        val nomeFilialUsuario = SessionManager.usuarioLogado?.academia?.trim() ?: return null

        return try {
            val academias = academiaRepository.getAcademias().first()
            val filial = academias
                .flatMap { it.filiais }
                .firstOrNull { it.nome.equals(nomeFilialUsuario, ignoreCase = true) }

            filial?._id
        } catch (e: Exception) {
            Log.e("GaleriaFotosViewModel", "Erro ao obter filial do usuário: ${e.message}")
            null
        }
    }

    fun carregarFotos(eventoId: String) {
        viewModelScope.launch {
            _uiState.update { GaleriaFotosUiState.Loading }
            try {
                val fotos = galeriaRepository.getFotosPorEvento(eventoId).first()
                _uiState.update {
                    if (fotos.isEmpty()) GaleriaFotosUiState.Empty
                    else GaleriaFotosUiState.Success(fotos)
                }
            } catch (e: Exception) {
                _uiState.update { GaleriaFotosUiState.Error("Erro ao carregar fotos: ${e.message}") }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun baixarFotosSelecionadas(
        context: Context,
        selectedIds: Set<String>,
        todasFotos: List<GaleriaFoto>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val fotosSelecionadas = todasFotos.filter { it._id in selectedIds }

                if (fotosSelecionadas.isEmpty()) {
                    onError("Nenhuma foto encontrada com os IDs selecionados.")
                    return@launch
                }

                val client = OkHttpClient()

                withContext(Dispatchers.IO) {
                    for (foto in fotosSelecionadas) {
                        try {
                            val request = Request.Builder().url(foto.url).build()
                            val response = client.newCall(request).execute()

                            if (!response.isSuccessful) continue

                            val bytes = response.body?.bytes() ?: continue

                            val nomeArquivo = when {
                                !foto.nomeArquivo.isNullOrBlank() -> {
                                    if (foto.nomeArquivo.endsWith(".jpg")) foto.nomeArquivo
                                    else "${foto.nomeArquivo}.jpg"
                                }

                                else -> "${foto._id}.jpg"
                            }

                            val contentValues = ContentValues().apply {
                                put(MediaStore.Downloads.DISPLAY_NAME, nomeArquivo)
                                put(MediaStore.Downloads.MIME_TYPE, "image/jpeg")
                                put(
                                    MediaStore.Downloads.RELATIVE_PATH,
                                    Environment.DIRECTORY_DOWNLOADS
                                )
                                put(MediaStore.Downloads.IS_PENDING, 1)
                            }

                            val resolver = context.contentResolver
                            val uri = resolver.insert(
                                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                                contentValues
                            )

                            if (uri != null) {
                                resolver.openOutputStream(uri)?.use { outputStream ->
                                    outputStream.write(bytes)
                                    outputStream.flush()
                                }

                                contentValues.clear()
                                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                                resolver.update(uri, contentValues, null, null)
                            }
                        } catch (_: Exception) {
                            // Ignora erro individual
                        }
                    }
                }

                onSuccess()
            } catch (e: Exception) {
                onError("Erro ao baixar fotos: ${e.message}")
            }
        }
    }

    fun deletarFoto(fotoId: String, eventoId: String) {
        viewModelScope.launch {
            try {
                galeriaRepository.deletarFotos(listOf(fotoId), eventoId) // Envia lista com 1 ID
                carregarFotos(eventoId)
            } catch (e: Exception) {
                _uiState.update { GaleriaFotosUiState.Error("Erro ao deletar foto: ${e.message}") }
            }
        }
    }

    fun correctImageRotation(context: Context, uri: Uri): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri) ?: throw Exception("InputStream nulo")
        val exif = ExifInterface(inputStream)

        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val rotationAngle = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }

        inputStream.close()

        val bitmapStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(bitmapStream)
        bitmapStream?.close()

        return if (rotationAngle != 0f) {
            val matrix = Matrix().apply { postRotate(rotationAngle) }
            Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, true)
        } else {
            originalBitmap
        }
    }

    fun deletarFotosSelecionadas(fotoIds: List<String>, eventoId: String) {
        viewModelScope.launch {
            try {
                galeriaRepository.deletarFotos(fotoIds, eventoId)
                carregarFotos(eventoId)
            } catch (e: Exception) {
                _uiState.update { GaleriaFotosUiState.Error("Erro ao deletar fotos: ${e.message}") }
            }
        }
    }

    private fun compressImage(context: Context, uri: Uri): File {
        val bitmap = correctImageRotation(context, uri)

        // Redimensionar mantendo proporção
        val targetWidth = 1280
        val targetHeight = (bitmap.height * (1280.0 / bitmap.width)).toInt()
        val resizedBitmap = bitmap.scale(targetWidth, targetHeight)

        val compressedFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
        FileOutputStream(compressedFile).use { out ->
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
        }

        return compressedFile
    }

    fun uploadFotos(
        context: Context,
        eventoId: String,
        academiaId: String,
        usuarioId: String,
        imageUris: List<Uri>,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                Log.d("UploadFotos", "Iniciando upload de ${imageUris.size} imagens")

                val initialStates = imageUris.associate { uri ->
                    uri.toString() to UploadUiState.Idle
                }
                _uploadUiState.update { initialStates }

                imageUris.forEach { uri ->
                    try {
                        Log.d("UploadFotos", "Processando imagem: $uri")

                        _uploadUiState.update { currentStates ->
                            currentStates + (uri.toString() to UploadUiState.Uploading(0f))
                        }

                        val fileName = getFileName(context, uri)
                            ?: "image_${System.currentTimeMillis()}.jpg"

                        // COMPACTAÇÃO AQUI
                        val compressedFile = compressImage(context, uri)

                        for (progress in 1..10) {
                            _uploadUiState.update { currentStates ->
                                currentStates + (uri.toString() to UploadUiState.Uploading(progress / 10f))
                            }
                            kotlinx.coroutines.delay(100)
                        }

                        val requestFile =
                            compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val photoPart = MultipartBody.Part.createFormData(
                            "foto",
                            fileName,
                            requestFile
                        )

                        Log.d(
                            "UploadFotos",
                            "Enviando foto: $fileName (size: ${compressedFile.length()} bytes)"
                        )

                        galeriaRepository.enviarFotos(
                            eventoId = eventoId,
                            academiaId = academiaId.toRequestBody(),
                            usuarioId = usuarioId.toRequestBody(),
                            fotos = listOf(photoPart)
                        )

                        Log.d("UploadFotos", "Upload concluído para: $fileName")

                        _uploadUiState.update { currentStates ->
                            currentStates + (uri.toString() to UploadUiState.Success)
                        }

                        compressedFile.delete()

                    } catch (e: Exception) {
                        Log.e("UploadFotos", "Erro ao fazer upload da imagem $uri: ${e.message}", e)
                        _uploadUiState.update { currentStates ->
                            currentStates + (uri.toString() to UploadUiState.Error(
                                e.message ?: "Erro no upload"
                            ))
                        }
                    }
                }

                Log.d("UploadFotos", "Recarregando fotos do evento $eventoId")
                carregarFotos(eventoId)

                kotlinx.coroutines.delay(2000)
                _uploadUiState.update { emptyMap() }

                Log.d("UploadFotos", "Upload completo. Chamando onComplete()")
                onComplete()

            } catch (e: Exception) {
                Log.e("UploadFotos", "Erro global no upload: ${e.message}", e)
                _uiState.update {
                    GaleriaFotosUiState.Error("Erro no upload: ${e.message}")
                }
            }
        }
    }


    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex >= 0) {
                        result = it.getString(columnIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result
    }
}