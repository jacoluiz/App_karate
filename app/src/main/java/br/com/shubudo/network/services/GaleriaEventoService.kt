package br.com.shubudo.network.services

import br.com.shubudo.database.entities.GaleriaEventoEntity
import br.com.shubudo.model.GaleriaEvento
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class GaleriaEventoRequest(
    val titulo: String,
    val descricao: String,
    val data: String,
    val academiaId: String,
    val filialId: String,
    val criadoPor: String
)

data class GaleriaEventoResponse(
    val _id: String?,
    val titulo: String,
    val descricao: String?,
    val data: String,
    val academiaId: String,
    val filialId: String,
    val criadoPor: String,
    val createdAt: String
)

fun GaleriaEventoResponse.toGaleriaEventoEntity(): GaleriaEventoEntity {
    return GaleriaEventoEntity(
        _id = this._id ?: "",
        titulo = this.titulo,
        descricao = this.descricao ?: "",
        data = this.data,
        criadoPor = this.criadoPor,
        filialId = this.filialId,
        academiaId = this.academiaId,
        createdAt = this.createdAt ?: ""
    )
}

interface GaleriaEventoService {

    @GET("/galeria/eventos")
    suspend fun listarEventos(): List<GaleriaEventoResponse> // <--- Aqui

    @POST("/galeria/eventos")
    suspend fun criarEvento(@Body evento: GaleriaEventoRequest): GaleriaEventoResponse // <---

    @PUT("/galeria/eventos/{id}")
    suspend fun atualizarEvento(
        @Path("id") eventoId: String,
        @Body evento: GaleriaEventoRequest
    ): GaleriaEventoResponse // <---

    @DELETE("/galeria/eventos/{id}")
    suspend fun deletarEvento(@Path("id") eventoId: String)
}

