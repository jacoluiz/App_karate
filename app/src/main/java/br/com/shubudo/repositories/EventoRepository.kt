package br.com.shubudo.repositories

import br.com.shubudo.database.dao.EventoDao
import br.com.shubudo.database.entities.toEvento
import br.com.shubudo.model.Evento
import br.com.shubudo.network.services.EventoResponse
import br.com.shubudo.network.services.EventoService
import br.com.shubudo.network.services.NovoEventoRequest
import br.com.shubudo.network.services.toEventoEntity
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.OffsetDateTime
import javax.inject.Inject

class EventoRepository @Inject constructor(
    private val eventoService: EventoService,
    private val eventoDao: EventoDao
) {

    // Atualiza os dados do servidor no banco local
    suspend fun refreshEventos() {
        val eventosFromApi = eventoService.listarEventos()
        eventoDao.limparTodos()
        eventoDao.salvarTodos(*eventosFromApi.map { it.toEventoEntity() }.toTypedArray())
    }

    // Obtém os dados do banco local (como Flow)
    fun getEventos(): Flow<List<Evento>> {
        return eventoDao.getEventos().map { list -> list.map { it.toEvento() } }
    }

    // Obtém evento por ID
    suspend fun getEventoPorId(eventoId: String): Evento? {
        return eventoDao.getEventoPorId(eventoId)?.toEvento()
    }

    suspend fun confirmarPresenca(evento: Evento): Evento {
        val response = eventoService.atualizarEvento(evento._id, evento)

        if (response.isSuccessful) {
            val json = response.body()?.getAsJsonObject("data")
            val eventoResponse = Gson().fromJson(json, EventoResponse::class.java)
            val entity = eventoResponse.toEventoEntity()
            eventoDao.salvarTodos(entity)
            return entity.toEvento()
        } else {
            throw Exception("Erro ao atualizar evento: ${response.code()}")
        }
    }

    suspend fun deletarEvento(eventoId: String) {
        eventoService.deletarEvento(eventoId)
        eventoDao.deletarPorId(eventoId)
    }

    suspend fun criarEvento(
        titulo: String,
        descricao: String,
        dataInicio: String,
        local: String,
        academia: String
    ) {
        val novoEvento = NovoEventoRequest(
            titulo = titulo,
            descricao = descricao,
            dataInicio = dataInicio,
            local = local,
            academia = academia
        )
        eventoService.criarEvento(novoEvento)
        refreshEventos()
    }

    suspend fun editarEvento(
        eventoId: String,
        titulo: String,
        descricao: String,
        dataInicio: String,
        local: String,
        academia: String
    ) {
        val eventoAtualizado = NovoEventoRequest(
            titulo = titulo,
            descricao = descricao,
            dataInicio = dataInicio,
            local = local,
            academia = academia
        )
        eventoService.editarEvento(eventoId, eventoAtualizado)
        refreshEventos()
    }

    fun getEventosFuturos(): Flow<List<Evento>> {
        return eventoDao.getEventos().map { entities ->
            val now = Instant.now()
            entities
                .map { it.toEvento() }
                .filter { ev -> isFutureOrNow(ev.dataInicio, now) }
        }
    }

    private fun isFutureOrNow(dataInicio: String?, now: Instant): Boolean {
        if (dataInicio.isNullOrBlank()) return false
        return try {
            // Ex.: 2025-08-17T15:00:00.000+00:00
            val odt = OffsetDateTime.parse(dataInicio)
            odt.toInstant() >= now
        } catch (_: Exception) {
            false
        }
    }
}
