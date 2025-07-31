package br.com.shubudo.repositories

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import br.com.shubudo.network.services.RelatorioService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class RelatorioRepository @Inject constructor(
    private val service: RelatorioService
) {

    /**
     * Baixa o relatório e salva diretamente na pasta Downloads.
     * Retorna o Uri do arquivo salvo.
     */
    suspend fun baixarESalvarRelatorioOrganizado(
        context: Context,
        fileName: String = "relatorio-organizado.xlsx"
    ): Uri = withContext(Dispatchers.IO) {
        val response = service.baixarRelatorioOrganizado()
        if (!response.isSuccessful) {
            throw Exception("Falha ao baixar relatório: ${response.code()} ${response.message()}")
        }
        val body = response.body() ?: throw Exception("Corpo da resposta vazio")

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(
                        MediaStore.Downloads.MIME_TYPE,
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    )
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }

                val uri = resolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    values
                ) ?: throw Exception("Não foi possível criar o arquivo no Downloads")

                resolver.openOutputStream(uri)?.use { out ->
                    body.byteStream().use { input -> input.copyTo(out) }
                } ?: throw Exception("Falha ao abrir OutputStream")

                values.clear()
                values.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(uri, values, null, null)

                uri
            } else {
                // Pré-Android 10: requer WRITE_EXTERNAL_STORAGE concedida
                val dir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!dir.exists()) dir.mkdirs()
                val file = File(dir, fileName)
                FileOutputStream(file).use { out ->
                    body.byteStream().use { input -> input.copyTo(out) }
                }
                Uri.fromFile(file)
            }
        } finally {
            body.close()
        }
    }
}
