package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.AvisoDao
import br.com.shubudo.database.entities.toAviso
import br.com.shubudo.model.Aviso
import br.com.shubudo.network.services.AvisoService
import br.com.shubudo.network.services.toAviso
import br.com.shubudo.network.services.toAvisoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AvisoRepository @Inject constructor(
    private val service: AvisoService,
    private val dao: AvisoDao
) {

    /**
     * Retorna a lista de avisos salvos localmente como Flow.
     * Pode ser usado para observar atualizações na base de dados.
     */
    suspend fun getAvisos(): Flow<List<Aviso>> {
        return dao.getAvisos().map { list ->
            list.map { it.toAviso() }
        }
    }

    /**
     * Busca um aviso específico pelo ID diretamente na API.
     */
    suspend fun getAvisoById(id: String): Aviso? {
        return try {
            val response = service.getAvisoById(id)
            response.toAviso()
        } catch (e: Exception) {
            Log.e("AvisoRepository", "Erro ao buscar aviso por id: ${e.message}", e)
            null
        }
    }

    /**
     * Cria um novo aviso através da API e salva no banco de dados local.
     */
    suspend fun criarAviso(aviso: Aviso): Aviso? {
        return try {
            // Chama o serviço para criar o aviso
            val response = service.criarAviso(aviso)
            // Converte a resposta para entidade e salva localmente
            val entity = response.toAvisoEntity()
            dao.salvarAviso(entity)
            // Retorna o aviso convertido para o model de domínio
            response.toAviso()
        } catch (e: Exception) {
            Log.e("AvisoRepository", "Erro ao criar aviso: ${e.message}", e)
            null
        }
    }

    /**
     * Atualiza um aviso existente na API e reflete as alterações no banco local.
     */
    suspend fun atualizarAviso(aviso: Aviso): Aviso? {
        return try {
            // Chama o serviço para atualizar o aviso, utilizando seu ID
            val response = service.atualizarAviso(aviso._id, aviso)
            response?.let {
                val updatedEntity = it.toAvisoEntity()
                dao.atualizarAviso(updatedEntity)
                it.toAviso()
            }
        } catch (e: Exception) {
            Log.e("AvisoRepository", "Erro ao atualizar aviso: ${e.message}", e)
            null
        }
    }

    /**
     * Exclui um aviso na API e remove do banco de dados local.
     */
    suspend fun excluirAviso(avisoId: String): Boolean {
        return try {
            // Chama o serviço para excluir o aviso
            service.excluirAviso(avisoId)
            // Remove o aviso do banco local
            dao.deletarAviso(avisoId)
            true
        } catch (e: Exception) {
            Log.e("AvisoRepository", "Erro ao excluir aviso: ${e.message}", e)
            false
        }
    }
}
