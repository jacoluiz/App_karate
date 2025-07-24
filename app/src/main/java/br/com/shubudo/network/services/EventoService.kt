package br.com.shubudo.network.services

import br.com.shubudo.database.entities.EventoEntity
import br.com.shubudo.model.Evento
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class EventoResponse(
    val _id: String,
    val titulo: String,
    val descricao: String,
    val dataInicio: String,
    val local: String?,
    val confirmados: List<String>? = emptyList()
)

data class NovoEventoRequest(
    val titulo: String,
    val descricao: String,
    val dataInicio: String,
    val local: String
)
fun EventoResponse.toEvento(): Evento {
    return Evento(
        _id = _id,
        titulo = titulo,
        descricao = descricao,
        dataInicio = dataInicio,
        local = local ?: "",
        confirmados = confirmados ?: emptyList()
    )
}

fun EventoResponse.toEventoEntity(): EventoEntity {
    return EventoEntity(
        _id = _id,
        titulo = titulo,
        descricao = descricao,
        dataInicio = dataInicio,
        local = local ?: "",
        confirmados = confirmados ?: emptyList()
    )
}

interface EventoService {
    @GET("/datas")
    suspend fun listarEventos(): List<EventoResponse>

    @PUT("/datas/{id}")
    suspend fun atualizarEvento(
        @Path("id") eventoId: String,
        @Body request: Evento
    ): Response<JsonObject>

    @POST("/datas")
    suspend fun criarEvento(@Body evento: NovoEventoRequest): EventoResponse

    @DELETE("/datas/{id}")
    suspend fun deletarEvento(@Path("id") eventoId: String)

    @PUT("/datas/{id}")
    suspend fun editarEvento(
        @Path("id") eventoId: String,
        @Body evento: NovoEventoRequest
    ): EventoResponse
}
