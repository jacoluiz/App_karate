package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.ProjecaoDao
import br.com.shubudo.database.entities.toProjecao
import br.com.shubudo.database.entities.toSequenciaDeCombate
import br.com.shubudo.model.Projecao
import br.com.shubudo.network.services.ProjecaoServices
import br.com.shubudo.network.services.toProjecaoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.ConnectException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ProjecoesRepository @Inject constructor(
    private val projecoesService: ProjecaoServices,
    private val projecoesDao: ProjecaoDao
) {

    suspend fun findAllProjecoes(): Flow<List<Projecao>> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = projecoesService.getProjecoes()
                val entities = response.map { it.toProjecaoEntity() }
                projecoesDao.saveAll(entities)
            } catch (e: ConnectException) {
                Log.e("ProjecoesRepository", "findAllProjecoes: falha ao conectar na API", e)
            }
        }

        return projecoesDao.getAll().map { entities ->
            entities.map { it.toProjecao() }
        }


    }

    suspend fun findByFaixa(faixaId: String): Flow<List<Projecao>> {
        // Sincronizar dados com a API
        try {
            val response = projecoesService.getProjecoes()
            val entities = response.map { it.toProjecaoEntity() }
            projecoesDao.saveAll(entities) // Salvar no banco de dados
        } catch (e: ConnectException) {
            Log.e("ProjecaoRepository", "findByFaixa: Falha ao conectar na API", e)
        }

        // Buscar dados do banco de dados local
        return projecoesDao.getByFaixa(faixaId).map { entities ->
            entities.map { it.toProjecao() } // Converter entidades para o modelo esperado
        }
    }

}
