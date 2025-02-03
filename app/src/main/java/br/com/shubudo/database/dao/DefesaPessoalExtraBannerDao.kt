package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.DefesaPessoalExtraBannerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DefesaPessoalExtraBannerDao {

    @Query("SELECT * FROM defesas_pessoais_extra_banner WHERE faixa = :faixa")
    fun getDefesasPessoaisExtraBannerByFaixa(faixa: String): Flow<List<DefesaPessoalExtraBannerEntity>>

    // Buscar defesa por ID
    @Query("SELECT * FROM defesas_pessoais_extra_banner WHERE _id = :id")
    suspend fun buscarPorId(id: String): DefesaPessoalExtraBannerEntity?

    // Listar todas as defesas
    @Query("SELECT * FROM defesas_pessoais_extra_banner")
    fun listarTodas(): Flow<List<DefesaPessoalExtraBannerEntity>>

    // Salvar ou atualizar defesa
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarDefesa(defesa: DefesaPessoalExtraBannerEntity)

    // Excluir defesa por ID
    @Query("DELETE FROM defesas_pessoais_extra_banner WHERE _id = :id")
    suspend fun excluirDefesaPorId(id: String)

    // Excluir todas as defesas
    @Query("DELETE FROM defesas_pessoais_extra_banner")
    suspend fun excluirTodasDefesas()

    // Salvar ou atualizar uma lista de defesas
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarTodasDefesas(defesas: List<DefesaPessoalExtraBannerEntity>)

}

