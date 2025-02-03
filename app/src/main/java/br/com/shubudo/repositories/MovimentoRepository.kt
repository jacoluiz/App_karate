package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.MovimentoDao
import br.com.shubudo.database.entities.toMovimento
import br.com.shubudo.model.Movimento
import br.com.shubudo.network.services.MovimentoService
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

    suspend fun findMovimentoByFaixa(faixa: String, tipoMovimento: String): Flow<List<Movimento>> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = if (tipoMovimento == "Ataque de mão") {
                    service.getAtaquesDeMao()
                } else if (tipoMovimento == "Chute") {
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

        return dao.getMovimentosByFaixaETipo(faixa, tipoMovimento).map { entities ->
            entities.map { it.toMovimento() }
        }
    }
}