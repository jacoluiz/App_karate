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
     * Baixa o relatório de organização (cones/filas) por EVENTO e salva em Downloads.
     */
    suspend fun baixarESalvarRelatorioOrganizado(
        context: Context,
        eventoId: String,
        conesMax: Int? = null,
        filasMax: String? = null,
        fileName: String = "relatorio-organizado.xlsx"
    ): Uri = withContext(Dispatchers.IO) {
        val response = service.baixarRelatorioOrganizado(
            eventoId = eventoId,
            conesMax = conesMax,
            filasMax = filasMax
        )
        if (!response.isSuccessful) {
            throw Exception("Falha ao baixar relatório: ${response.code()} ${response.message()}")
        }

        val body = response.body() ?: throw Exception("Corpo da resposta vazio")

        try {
            salvarXlsxEmDownloads(context, body.byteStream(), fileName)
        } finally {
            body.close()
        }
    }


    /**
     * Baixa o relatório de EXAME por evento (adultos/adolescentes ou 1ª infância)
     * e salva em Downloads.
     *
     * @param eventoId            ID do evento
     * @param primeiraInfancia    true = usar template de 1ª infância, false = demais
     * @param fileName            Nome do arquivo a salvar
     */
    suspend fun baixarESalvarRelatorioExamePorEvento(
        context: Context,
        eventoId: String,
        primeiraInfancia: Boolean,
        fileName: String = "relatorio-${if (primeiraInfancia) "primeira-infancia" else "exame"}-$eventoId.xlsx"
    ): Uri = withContext(Dispatchers.IO) {
        val response = if (primeiraInfancia) {
            // Chama a rota /relatorios/primeira-infancia/{eventoId}
            service.baixarRelatorioPrimeiraInfanciaPorEvento(eventoId)
        } else {
            // Chama a rota /relatorios/exame/{eventoId}
            service.baixarRelatorioExamePorEvento(eventoId, false)
        }

        if (!response.isSuccessful) {
            throw Exception("Falha ao baixar relatório por evento: ${response.code()} ${response.message()}")
        }
        val body = response.body() ?: throw Exception("Corpo da resposta vazio")

        try {
            salvarXlsxEmDownloads(context, body.byteStream(), fileName)
        } finally {
            body.close()
        }
    }


    // ---- Helpers ----

    private fun salvarXlsxEmDownloads(
        context: Context,
        inputStream: java.io.InputStream,
        fileName: String
    ): Uri {
        val mime =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, mime)
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val uri = resolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                values
            ) ?: throw Exception("Não foi possível criar o arquivo no Downloads")

            resolver.openOutputStream(uri)?.use { out ->
                inputStream.use { it.copyTo(out) }
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
                inputStream.use { it.copyTo(out) }
            }
            Uri.fromFile(file)
        }
    }
}
