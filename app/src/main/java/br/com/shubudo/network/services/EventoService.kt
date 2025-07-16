package br.com.shubudo.network.services

import br.com.shubudo.database.entities.EventoEntity
import br.com.shubudo.model.Evento
import retrofit2.http.GET

data class EventoResponse(
    val _id: String,
    val titulo: String,
    val descricao: String,
    val dataInicio: String,
    val local: String?
)

fun EventoResponse.toEvento(): Evento {
    return Evento(
        _id = _id,
        titulo = titulo,
        descricao = descricao,
        dataInicio = dataInicio,
        local = local ?: ""
    )
}

fun EventoResponse.toEventoEntity(): EventoEntity {
    return EventoEntity(
        _id = _id,
        titulo = titulo,
        descricao = descricao,
        dataInicio = dataInicio,
        local = local ?: ""
    )
}

interface EventoService {
    @GET("/datas")
    suspend fun listarEventos(): List<EventoResponse>
}
