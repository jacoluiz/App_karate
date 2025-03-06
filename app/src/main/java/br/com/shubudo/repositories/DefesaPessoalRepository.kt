package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.DefesaPessoalDao
import br.com.shubudo.database.entities.toDefesaPessoal
import br.com.shubudo.model.DefesaPessoal
import br.com.shubudo.network.services.DefesaPessoalServices
import br.com.shubudo.network.services.toDefesaPessoalEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.ConnectException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class DefesaPessoalRepository @Inject constructor(
    private val service: DefesaPessoalServices,
    private val dao: DefesaPessoalDao
) {
    suspend fun findByFaixa(faixa: String): Flow<List<DefesaPessoal>> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = service.getDefesasPessoais()
                val entities = response.map { it.toDefesaPessoalEntity() }
                dao.saveAll(*entities.toTypedArray())
            } catch (e: ConnectException) {
                Log.e("DefesaPessoalRepository", "findSections: falha ao conectar na API", e)
            }
        }

        return dao.getDefesasPessoaisByFaixa(faixa).map { entities ->
            entities.map { it.toDefesaPessoal() }
        }
    }
}

