package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.KataDao
import br.com.shubudo.database.entities.toKata
import br.com.shubudo.model.Kata
import br.com.shubudo.network.services.KataServices
import br.com.shubudo.network.services.toKataEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.ConnectException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class KataRepository @Inject constructor(
    private val service: KataServices,
    private val dao: KataDao
) {
    suspend fun findByFaixa(faixa: String): Flow<List<Kata>> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = service.getKatas()
                val entities = response.map { it.toKataEntity() }
                dao.saveAll(*entities.toTypedArray())
            } catch (e: ConnectException) {
                Log.e("DefesaPessoalRepository", "findSections: falha ao conectar na API", e)
            }
        }

        return dao.getKataByFaixa(faixa).map { entities ->
            entities.map { it.toKata() }
        }
    }
}