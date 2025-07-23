package br.com.shubudo.repositories

import br.com.shubudo.database.dao.EventoDao
import br.com.shubudo.database.entities.toEvento
import br.com.shubudo.model.Evento
import br.com.shubudo.network.services.EventoResponse
import br.com.shubudo.network.services.EventoService
import br.com.shubudo.network.services.toEventoEntity
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
}
