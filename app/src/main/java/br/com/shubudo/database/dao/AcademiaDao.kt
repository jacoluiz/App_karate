package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.AcademiaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AcademiaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(academia: AcademiaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(vararg academias: AcademiaEntity)

    @Query("SELECT * FROM academias")
    fun getAcademias(): Flow<List<AcademiaEntity>>

    @Query("SELECT * FROM academias WHERE id = :id")
    suspend fun getAcademiaById(id: String): AcademiaEntity?

    @Query("DELETE FROM academias")
    suspend fun deleteAll()

    @Query("DELETE FROM academias WHERE id = :id")
    suspend fun deleteById(id: String)
}
