package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.SequenciaDeCombateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SequenciaDeCombateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(movimento: SequenciaDeCombateEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(vararg entities: SequenciaDeCombateEntity)

    @Query("SELECT * FROM SequenciaDeCombate")
    fun getSequenciaDeCombate(): Flow<List<SequenciaDeCombateEntity>>

    @Query("SELECT * FROM SequenciaDeCombate WHERE _id = :id")
    fun getSequenciaDeCombateById(id: String): SequenciaDeCombateEntity

    @Query("SELECT * FROM SequenciaDeCombate WHERE faixa = :id")
    fun getSequenciaDeCombateByFaixa(id: String): Flow<List<SequenciaDeCombateEntity>>
}