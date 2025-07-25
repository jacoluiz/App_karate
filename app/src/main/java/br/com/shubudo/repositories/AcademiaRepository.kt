package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.AcademiaDao
import br.com.shubudo.database.entities.toAcademia
import br.com.shubudo.model.Academia
import br.com.shubudo.model.Filial
import br.com.shubudo.network.services.AcademiaService
import br.com.shubudo.network.services.NovaAcademiaRequest
import br.com.shubudo.network.services.toAcademiaEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AcademiaRepository @Inject constructor(
    private val academiaService: AcademiaService,
    private val academiaDao: AcademiaDao
) {
    suspend fun refreshAcademias() {
        try {
            val academiasFromApi = academiaService.getAcademias()
            academiaDao.deleteAll()
            academiaDao.saveAll(*academiasFromApi.map { it.toAcademiaEntity() }.toTypedArray())
        } catch (e: Exception) {
            Log.e("AcademiaRepository", "Erro ao atualizar academias: ${e.message}", e)
            throw e
        }
    }

    suspend fun getAcademias(): Flow<List<Academia>> {
        refreshAcademias()
        return academiaDao.getAcademias().map { list -> list.map { it.toAcademia() } }
    }

    suspend fun getAcademiaById(id: String): Academia? {
        refreshAcademias()
        return academiaDao.getAcademiaById(id)?.toAcademia()
    }

    suspend fun criarAcademia(
        nome: String, descricao: String?, filiais: List<Filial>
    ) {
        Log.d("AcademiaRepository", "Criando nova academia: $nome")
        val novaAcademia = NovaAcademiaRequest(nome, descricao, filiais)
        try {
            academiaService.criarAcademia(novaAcademia)
            Log.d("AcademiaRepository", "Academia criada com sucesso")
        } catch (e: Exception) {
            Log.e("AcademiaRepository", "Erro ao criar academia: ${e.message}", e)
            throw e
        }
        refreshAcademias()
    }

    suspend fun editarAcademia(
        id: String,
        nome: String,
        descricao: String?,
        filiais: List<Filial>
    ) {
        Log.d("AcademiaRepository", "Editando academia com ID: $id")
        val academiaAtualizada = NovaAcademiaRequest(nome, descricao, filiais)
        try {
            academiaService.editarAcademia(id, academiaAtualizada)
            Log.d("AcademiaRepository", "Academia editada com sucesso")
        } catch (e: Exception) {
            Log.e("AcademiaRepository", "Erro ao editar academia: ${e.message}", e)
            throw e
        }
        refreshAcademias()
    }

    suspend fun deletarAcademia(id: String) {
        try {
            academiaService.deletarAcademia(id)
            academiaDao.deleteById(id)
        } catch (e: Exception) {
            Log.e("AcademiaRepository", "Erro ao deletar academia: ${e.message}", e)
            throw e
        }
    }
}
