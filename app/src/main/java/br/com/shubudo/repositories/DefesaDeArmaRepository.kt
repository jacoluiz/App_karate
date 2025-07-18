package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.DefesaArmaDao
import br.com.shubudo.database.entities.toArmamento
import br.com.shubudo.model.Armamento
import br.com.shubudo.network.services.DefesaArmaServices
import br.com.shubudo.network.services.toArmamentoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.ConnectException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class DefesaDeArmaRepository @Inject constructor(
    private val service: DefesaArmaServices, // Serviço específico para defesas de armas
    private val dao: DefesaArmaDao            // DAO específico para defesas de armas
) {

    suspend fun findAll(): Flow<List<Armamento>> {
        // Busca os dados na API e salva localmente
        CoroutineScope(coroutineContext).launch {
            try {
                val response = service.getDefesasArmas()
                // Converte a resposta para entidades compatíveis com o modelo Armamento
                val entities = response.map { it.toArmamentoEntity() }
                // Salva os dados no banco local (pode incluir lógica para remover dados antigos, se necessário)
                dao.saveAll(*entities.toTypedArray())
            } catch (e: ConnectException) {
                Log.e("DefesaArmaRepository", "findAll: falha ao conectar na API", e)
            }
        }
        // Retorna os dados salvos, convertendo as entidades para o modelo de domínio Armamento
        return dao.getDefesasArmas().map { entityList ->
            entityList.map { it.toArmamento() }
        }
    }

    // Função para buscar defesas de armas filtradas por faixa (por id da faixa)
    suspend fun findByFaixa(faixaId: String): Flow<List<Armamento>> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = service.getDefesasArmas()
                val entities = response.map { it.toArmamentoEntity() }
                dao.saveAll(*entities.toTypedArray())
            } catch (e: ConnectException) {
                Log.e("DefesaArmaRepository", "findByFaixa: falha ao conectar na API", e)
            }
        }
        return dao.getDefesasArmasByFaixa(faixaId).map { entityList ->
            entityList.map { it.toArmamento() }
        }
    }
}
