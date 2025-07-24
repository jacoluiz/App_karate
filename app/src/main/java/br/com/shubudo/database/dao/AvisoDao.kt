package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.AvisoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AvisoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(aviso: AvisoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(vararg avisos: AvisoEntity)

    @Query("SELECT * FROM avisos")
    fun getAvisos(): Flow<List<AvisoEntity>>

    @Query("SELECT * FROM avisos WHERE id = :id")
    suspend fun getAvisoById(id: String): AvisoEntity?

    @Query("DELETE FROM avisos")
    suspend fun deleteAll()

    @Query("DELETE FROM avisos WHERE id = :id")
    suspend fun deleteById(id: String)

}
