package br.com.shubudo.repositories

import br.com.shubudo.database.dao.GaleriaEventoDao
import br.com.shubudo.database.entities.toGaleriaEvento
import br.com.shubudo.model.GaleriaEvento
import br.com.shubudo.network.services.GaleriaEventoRequest
import br.com.shubudo.network.services.GaleriaEventoService
import br.com.shubudo.network.services.toGaleriaEventoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GaleriaEventoRepository @Inject constructor(
    private val service: GaleriaEventoService,
    private val dao: GaleriaEventoDao
) {
    suspend fun refreshEventos() {
        val eventos = service.listarEventos()
        dao.deletarTodos()
        dao.salvarEventos(eventos.map { it.toGaleriaEventoEntity() })
    }

    fun getEventos(): Flow<List<GaleriaEvento>> {
        return dao.listarEventos().map { list -> list.map { it.toGaleriaEvento() } }
    }

    suspend fun criarEvento(request: GaleriaEventoRequest) {
        service.criarEvento(request)
        refreshEventos()
    }

    suspend fun editarEvento(id: String, request: GaleriaEventoRequest) {
        service.atualizarEvento(id, request)
        refreshEventos()
    }

    suspend fun deletarEvento(id: String) {
        service.deletarEvento(id)
        refreshEventos()
    }
}
