package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.ArmamentoDao
import br.com.shubudo.database.entities.toArmamento
import br.com.shubudo.model.Armamento
import br.com.shubudo.network.services.ArmamentoServices
import br.com.shubudo.network.services.toArmamentoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.ConnectException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ArmamentoRepository @Inject constructor(
    private val service: ArmamentoServices,
    private val dao: ArmamentoDao
) {

    // Função para buscar Armamentos filtrando por faixa
    suspend fun findByFaixa(faixaId: String): Flow<List<Armamento>> {
        // Atualiza os dados com base na API
        CoroutineScope(coroutineContext).launch {
            try {
                val response = service.getArmamentos()
                val entities = response.map { it.toArmamentoEntity() }
                dao.saveAll(*entities.toTypedArray())
            } catch (e: ConnectException) {
                Log.e("ArmamentoRepository", "findByFaixa: falha ao conectar na API", e)
            }
        }
        // Retorna os dados filtrados pelo id da faixa, convertendo as entidades para o modelo de domínio
        return dao.getArmamentoByFaixa(faixaId).map { entityList ->
            entityList.map { it.toArmamento() }
        }
    }
}
