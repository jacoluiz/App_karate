package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.ProjecaoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjecaoDao {

    // Inserir todas as projeções (sobrescreve em caso de conflito)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(projecoes: List<ProjecaoEntity>)

    // Buscar todas as projeções
    @Query("SELECT * FROM projecoes ORDER BY ordem ASC")
    fun getAll(): Flow<List<ProjecaoEntity>>

    // Buscar projeção por ID
    @Query("SELECT * FROM projecoes WHERE _id = :id")
    fun getById(id: String): Flow<ProjecaoEntity>

    // Deletar todas as projeções
    @Query("DELETE FROM projecoes")
    suspend fun deleteAll()

    // Deletar projeção específica pelo ID
    @Query("DELETE FROM projecoes WHERE _id = :id")
    suspend fun deleteById(id: String)

    // Buscar projeções por faixa
    @Query("SELECT * FROM projecoes WHERE faixa = :faixa ORDER BY ordem ASC")
    fun getByFaixa(faixa: String): Flow<List<ProjecaoEntity>>

}
