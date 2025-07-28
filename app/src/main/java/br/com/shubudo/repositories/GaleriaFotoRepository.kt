package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.GaleriaFotoDao
import br.com.shubudo.database.entities.toGaleriaFoto
import br.com.shubudo.model.GaleriaFoto
import br.com.shubudo.network.services.GaleriaFotoService
import br.com.shubudo.network.services.toGaleriaFotoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class GaleriaFotoRepository @Inject constructor(
    private val service: GaleriaFotoService,
    private val dao: GaleriaFotoDao
) {
    suspend fun listarPorEvento(eventoId: String) {
        val fotos = service.listarFotosPorEvento(eventoId)
        dao.removerPorEvento(eventoId)
        dao.salvarFotos(fotos.map { it.toGaleriaFotoEntity() })
    }

    suspend fun getFotosPorEvento(eventoId: String): Flow<List<GaleriaFoto>> {
        listarPorEvento(eventoId)
        return dao.listarFotosPorEvento(eventoId).map { list -> list.map { it.toGaleriaFoto() } }
    }

    suspend fun enviarFotos(
        eventoId: String,
        academiaId: RequestBody,
        usuarioId: RequestBody,
        fotos: List<MultipartBody.Part>
    ) {
        service.enviarFotos(eventoId, fotos, academiaId, usuarioId)
        listarPorEvento(eventoId)
    }

    suspend fun deletarFotos(fotoIds: List<String>, eventoId: String) {
        val body = hashMapOf("ids" to fotoIds)
        service.deletarFotos(body)
        listarPorEvento(eventoId)
    }
}
