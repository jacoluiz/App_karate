package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.AvisoDao
import br.com.shubudo.database.entities.toAviso
import br.com.shubudo.model.Aviso
import br.com.shubudo.model.AvisoResumido
import br.com.shubudo.network.services.AvisoService
import br.com.shubudo.network.services.toAviso
import br.com.shubudo.network.services.toAvisoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class AvisoRepository @Inject constructor(
    private val service: AvisoService,
    private val dao: AvisoDao
) {

    /**
     * Retorna a lista de avisos salvos localmente como Flow.
     * Pode ser usado para observar atualizações na base de dados.
     */
    suspend fun getAvisos(): Flow<List<Aviso>> {
        // Tenta buscar os avisos na API e salva no banco local.
        CoroutineScope(coroutineContext).launch {
            try {
                // Chama o endpoint para listar os avisos na API.
                val response = service.listarAvisos()
                // Converte cada resposta para entidade para salvar no banco.
                val entities = response.map { it.toAvisoEntity() }
                // Salva todas as entidades no banco de dados.
                dao.saveAll(*entities.toTypedArray())
            } catch (e: ConnectException) {
                Log.e("AvisoRepository", "getAvisos: Falha ao conectar na API", e)
            } catch (e: Exception) {
                Log.e("AvisoRepository", "getAvisos: Erro ao buscar avisos", e)
            }
        }

        // Retorna os avisos armazenados localmente convertidos para o model de domínio.
        return dao.getAvisos().map { list ->
            list.map { it.toAviso() }
        }
    }

    suspend fun refreshAvisos(): Flow<List<Aviso>> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Deleta todos os registros da tabela de avisos
                dao.deletarTodos()

                // 2. Busca os avisos da API
                val avisosResponse = service.listarAvisos() // Certifique-se de que esse endpoint retorne uma lista de avisos
                val avisosEntities = avisosResponse.map { it.toAvisoEntity() }

                // 3. Salva os novos avisos no banco de dados local
                dao.saveAll(*avisosEntities.toTypedArray())

                // 4. Retorna os avisos do banco convertidos para o modelo de domínio
                dao.getAvisos().map { list ->
                    list.map { it.toAviso() }
                }
            } catch (e: Exception) {
                Log.e("AvisoRepository", "Erro ao atualizar avisos: ${e.message}", e)
                throw e
            }
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

            val avisoResumido = AvisoResumido(
                titulo = aviso.titulo,
                conteudo = aviso.conteudo
            )

            Log.i("AvisoResumido", avisoResumido.toString())
            val response = service.criarAviso(avisoResumido)
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
