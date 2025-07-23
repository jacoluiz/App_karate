package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.SequenciaDeCombateDao
import br.com.shubudo.database.entities.toSequenciaDeCombate
import br.com.shubudo.model.SequenciaDeCombate
import br.com.shubudo.network.services.SequenciaDeCombateService
import br.com.shubudo.network.services.toSequenciaDeCombateEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.ConnectException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class SequenciaDeCombateRepository @Inject constructor(
    private val dao: SequenciaDeCombateDao,
    private val service: SequenciaDeCombateService
) {
    suspend fun findByFaixa(faixa: String): Flow<List<SequenciaDeCombate>> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = service.getSequenciaDeCombate()
                val entities = response.map { it.toSequenciaDeCombateEntity() }
                dao.saveAll(*entities.toTypedArray())
            } catch (e: ConnectException) {
                Log.e("DefesaPessoalRepository", "findSections: falha ao conectar na API", e)
            }
        }

        return dao.getSequenciaDeCombateByFaixa(faixa).map { entities ->
            entities.map { it.toSequenciaDeCombate() }
        }
    }
}