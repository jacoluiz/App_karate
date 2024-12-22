package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.DefesaPessoalExtraBannerDao
import br.com.shubudo.database.entities.toDefesaPessoalExtraBanner
import br.com.shubudo.model.DefesaPessoal
import br.com.shubudo.model.DefesaPessoalExtraBanner
import br.com.shubudo.network.services.DefesaPessoalExtraBannerServices
import br.com.shubudo.network.services.toDefesaPessoalExtraBannerEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.ConnectException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class DefesaPessoalExtraBannerRepository @Inject constructor(
    private val extraBannerService: DefesaPessoalExtraBannerServices,
    private val extraBannerDao: DefesaPessoalExtraBannerDao
) {

    suspend fun findAll(): Flow<List<DefesaPessoalExtraBanner>> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = extraBannerService.getDefesasPessoaisExtraBanner()
                val entities = response.map { it.toDefesaPessoalExtraBannerEntity() }
                extraBannerDao.salvarTodasDefesas(entities) // Passa a lista diretamente
            } catch (e: ConnectException) {
                Log.e("ExtraBannerRepository", "findAllExtraBanner: falha ao conectar na API", e)
            }
        }

        // Retorna os dados do banco local mapeados para o modelo esperado
        return extraBannerDao.listarTodas().map { entities ->
            entities.map { it.toDefesaPessoalExtraBanner() }
        }
    }


    suspend fun findByFaixa(faixa: String): Flow<List<DefesaPessoalExtraBanner>> {
        findAll() // Chama o método para buscar todos os dados
        return extraBannerDao.getDefesasPessoaisExtraBannerByFaixa(faixa).map { entities ->
            entities.map { it.toDefesaPessoalExtraBanner() }
        }
    }
}
