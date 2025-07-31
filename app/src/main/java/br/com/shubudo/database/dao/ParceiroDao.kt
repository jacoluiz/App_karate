package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.ParceiroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ParceiroDao {

    @Query("DELETE FROM Parceiro")
    suspend fun limparTodos()

    @Query("SELECT * FROM Parceiro ORDER BY nome")
    fun getParceiros(): Flow<List<ParceiroEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarTodos(vararg parceiros: ParceiroEntity)

    @Query("SELECT * FROM Parceiro WHERE _id = :id")
    suspend fun getParceiroPorId(id: String): ParceiroEntity?

    @Query("DELETE FROM Parceiro WHERE _id = :id")
    suspend fun deletarPorId(id: String)
}
