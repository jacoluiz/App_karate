package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.MovimentoDao
import br.com.shubudo.database.entities.toDefesaPessoal
import br.com.shubudo.database.entities.toMovimento
import br.com.shubudo.model.DefesaPessoal
import br.com.shubudo.model.Movimento
import br.com.shubudo.network.services.MovimentoService
import br.com.shubudo.network.services.toDefesaPessoalEntity
import br.com.shubudo.network.services.toMovimentoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.ConnectException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class MovimentoRepository @Inject constructor(
    private val dao: MovimentoDao,
    private val service: MovimentoService
) {

    suspend fun findAllAtaquesDeMao(): Flow<List<Movimento>> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = service.getAtaquesDeMao()
                val entities = response.map { it.toMovimentoEntity() }
                dao.saveAll(*entities.toTypedArray())
            } catch (e: ConnectException) {
                Log.e("MovimentoRepository", "findAll: falha ao conectar na API", e)
            }
        }

        return dao.getMovimentos().map { entities ->
            entities.map { it.toMovimento() }
        }
    }

    suspend fun findAllChutes(): Flow<List<Movimento>> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = service.getChutes()
                val entities = response.map { it.toMovimentoEntity() }
                dao.saveAll(*entities.toTypedArray())
            } catch (e: ConnectException) {
                Log.e("MovimentoRepository", "findAll: falha ao conectar na API", e)
            }
        }

        return dao.getMovimentos().map { entities ->
            entities.map { it.toMovimento() }
        }
    }

    suspend fun findAllDefesas(): Flow<List<Movimento>> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = service.getDefesas()
                val entities = response.map { it.toMovimentoEntity() }
                dao.saveAll(*entities.toTypedArray())
            } catch (e: ConnectException) {
                Log.e("MovimentoRepository", "findAll: falha ao conectar na API", e)
            }
        }

        return dao.getMovimentos().map { entities ->
            entities.map { it.toMovimento() }
        }
    }

    suspend fun findMovimentoByFaixa(faixa: String, tipoMovimento: String): Flow<List<Movimento>> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = if (tipoMovimento == "ataqueMao") {
                    service.getAtaquesDeMao()
                } else if (tipoMovimento == "chute") {
                    service.getChutes()
                } else {
                    service.getDefesas()
                }
                val entities = response.map { it.toMovimentoEntity() }
                dao.saveAll(*entities.toTypedArray())
            } catch (e: ConnectException) {
                Log.e("MovimentoRepository", "findSections: falha ao conectar na API", e)
            }
        }

        return dao.getMovimentosByFaixa(faixa).map { entities ->
            entities.map { it.toMovimento() }
        }
    }
}