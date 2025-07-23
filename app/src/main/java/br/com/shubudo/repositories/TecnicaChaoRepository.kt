package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.TecnicaChaoDao
import br.com.shubudo.database.entities.toTecnicaChao
import br.com.shubudo.model.TecnicaChao
import br.com.shubudo.network.services.TecnicaChaoService
import br.com.shubudo.network.services.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.ConnectException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class TecnicaChaoRepository @Inject constructor(
    private val service: TecnicaChaoService,
    private val dao: TecnicaChaoDao
) {

    suspend fun findByFaixa(faixaId: String): Flow<List<TecnicaChao>> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = service.getTecnicasChao()
                val entities = response.map { it.toEntity() }
                dao.saveAll(*entities.toTypedArray())
            } catch (e: ConnectException) {
                Log.e("TecnicaChaoRepository", "Erro ao sincronizar técnicas de chão", e)
            }
        }
        return dao.getTecnicasChaoByFaixa(faixaId).map { list ->
            list.map { it.toTecnicaChao() }
        }
    }
}
