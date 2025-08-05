package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.AvisoDao
import br.com.shubudo.database.entities.toAviso
import br.com.shubudo.model.Aviso
import br.com.shubudo.network.services.AvisoService
import br.com.shubudo.network.services.NovoAvisoRequest
import br.com.shubudo.network.services.toAvisoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AvisoRepository @Inject constructor(
    private val avisoService: AvisoService,
    private val avisoDao: AvisoDao
) {

    // Atualiza os dados da API e salva localmente
    suspend fun refreshAvisos() {
        val avisosFromApi = avisoService.getAvisos()
        avisoDao.deleteAll()
        avisoDao.saveAll(*avisosFromApi.map { it.toAvisoEntity() }.toTypedArray())
    }

    // Obtém avisos locais do Room como Flow
    fun getAvisos(): Flow<List<Aviso>> {
        return avisoDao.getAvisos().map { list -> list.map { it.toAviso() } }
    }

    // Cria um novo aviso
    suspend fun criarAviso(titulo: String, conteudo: String, publicoAlvo: List<String>, academia: String) {
        val novoAviso = NovoAvisoRequest(
            titulo = titulo,
            conteudo = conteudo,
            publicoAlvo = publicoAlvo,
            academia = academia
        )
        try {
            val avisoCriado = avisoService.criarAviso(novoAviso)
            Log.d("AvisoRepository", "Aviso criado com sucesso: $avisoCriado")
        } catch (e: Exception) {
            Log.e("AvisoRepository", "Erro ao criar aviso: ${e.message}", e)
            throw e
        }
        refreshAvisos()
    }

    // Edita um aviso existente
    suspend fun editarAviso(id: String, titulo: String, conteudo: String, publicoAlvo: List<String>,academia: String) {
        val avisoAtualizado = NovoAvisoRequest(
            titulo = titulo,
            conteudo = conteudo,
            publicoAlvo = publicoAlvo,
            academia = academia
        )
        try {
            avisoService.editarAviso(id, avisoAtualizado)
            Log.d("AvisoRepository", "Aviso editado com sucesso")
        } catch (e: Exception) {
            Log.e("AvisoRepository", "Erro ao editar aviso: ${e.message}", e)
            throw e
        }
        refreshAvisos()
    }

    // Deleta aviso por ID
    suspend fun deletarAviso(id: String) {
        avisoService.deletarAviso(id)
        avisoDao.deleteById(id) // remove localmente
    }

    // Busca aviso localmente por ID
    suspend fun getAvisoById(id: String): Aviso? {
        return avisoDao.getAvisoById(id)?.toAviso()
    }
}
