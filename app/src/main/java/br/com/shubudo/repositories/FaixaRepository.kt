package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.FaixaDao
import br.com.shubudo.database.entities.toFaixa
import br.com.shubudo.model.Faixa
import br.com.shubudo.network.services.FaixasServices
import br.com.shubudo.network.services.toFaixaEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.ConnectException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class FaixaRepository @Inject constructor(
    private val service: FaixasServices,
    private val dao: FaixaDao
) {
    suspend fun findAll(): Flow<List<Faixa>> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = service.getFaixas()
                val entities = response.map { it.toFaixaEntity() }
                dao.saveAll(*entities.toTypedArray())
            } catch (e: ConnectException) {
                Log.e("FaixaRepository", "findSections: falha ao conectar na API", e)
            }
        }

        return dao.getFaixas().map { entityList ->
            entityList.map { it.toFaixa() }
        }
    }

    suspend fun findById(id: String): Flow<Faixa> {
        try {
            val response = service.getFaixas()
            val entities = response.map { it.toFaixaEntity() }
            dao.saveAll(*entities.toTypedArray())
        } catch (e: ConnectException) {
            Log.e("FaixaRepository", "findSections: falha ao conectar na API", e)
        }

        return dao.getFaixaById(id).map { it.toFaixa() }
    }

    suspend fun findByNome(cor: String): Flow<Faixa> {
        try {
            val response = service.getFaixas()
            val entities = response.map { it.toFaixaEntity() }
            dao.saveAll(*entities.toTypedArray())
        } catch (e: ConnectException) {
            Log.e("FaixaRepository", "findSections: falha ao conectar na API", e)
        }

        return dao.getFaixaByCor(cor).map { it.toFaixa() }
    }
}

