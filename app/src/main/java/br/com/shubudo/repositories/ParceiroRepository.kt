package br.com.shubudo.repositories

import br.com.shubudo.database.dao.ParceiroDao
import br.com.shubudo.database.entities.toParceiro
import br.com.shubudo.model.Parceiro
import br.com.shubudo.network.services.NovoParceiroRequest
import br.com.shubudo.network.services.ParceiroService
import br.com.shubudo.network.services.toParceiroEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ParceiroRepository @Inject constructor(
    private val parceiroService: ParceiroService,
    private val parceiroDao: ParceiroDao
) {

    suspend fun refreshParceiros() {
        val parceirosFromApi = parceiroService.listarParceiros()
        parceiroDao.limparTodos()
        parceiroDao.salvarTodos(*parceirosFromApi.map { it.toParceiroEntity() }.toTypedArray())
    }

    fun getParceiros(): Flow<List<Parceiro>> {
        return parceiroDao.getParceiros().map { list -> list.map { it.toParceiro() } }
    }

    suspend fun getParceiroPorId(parceiroId: String): Parceiro? {
        return parceiroDao.getParceiroPorId(parceiroId)?.toParceiro()
    }

    suspend fun deletarParceiro(parceiroId: String) {
        parceiroService.deletarParceiro(parceiroId)
        parceiroDao.deletarPorId(parceiroId)
    }

    suspend fun criarParceiro(request: NovoParceiroRequest) {
        parceiroService.criarParceiro(request)
        refreshParceiros()
    }

    suspend fun editarParceiro(parceiroId: String, request: NovoParceiroRequest) {
        parceiroService.atualizarParceiro(parceiroId, request)
        refreshParceiros()
    }
}
