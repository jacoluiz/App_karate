package br.com.shubudo.repositories

import br.com.shubudo.database.dao.EventoDao
import br.com.shubudo.database.entities.toEvento
import br.com.shubudo.model.Evento
import br.com.shubudo.network.services.EventoService
import br.com.shubudo.network.services.toEventoEntity
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
}
