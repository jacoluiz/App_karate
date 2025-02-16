package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.shubudo.database.entities.AvisoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AvisoDao {

    // Método genérico para inserir ou substituir um aviso
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(aviso: AvisoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(vararg entities: AvisoEntity)

    // Recupera todos os avisos do banco de dados
    @Query("SELECT * FROM Aviso")
    fun getAvisos(): Flow<List<AvisoEntity>>

    // Recupera um aviso específico pelo ID
    @Query("SELECT * FROM Aviso WHERE _id = :id")
    fun getAvisoById(id: String): AvisoEntity

    // Recupera os avisos que estão ativos
    @Query("SELECT * FROM Aviso WHERE ativo = 1")
    fun getAvisosAtivos(): Flow<List<AvisoEntity>>

    // Busca avisos cujo campo exclusivoParaFaixas contenha a faixa informada
    @Query("SELECT * FROM Aviso WHERE exclusivoParaFaixas LIKE '%' || :faixa || '%'")
    fun getAvisosByFaixa(faixa: String): Flow<List<AvisoEntity>>

    // Método para salvar um aviso individualmente (usado pelo repositório)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarAviso(aviso: AvisoEntity)

    // Método para atualizar um aviso existente
    @Update
    suspend fun atualizarAviso(aviso: AvisoEntity)

    // Método para deletar um aviso pelo ID
    @Query("DELETE FROM Aviso WHERE _id = :id")
    suspend fun deletarAviso(id: String)

    @Query("DELETE FROM Aviso")
    suspend fun deletarTodos()

}
