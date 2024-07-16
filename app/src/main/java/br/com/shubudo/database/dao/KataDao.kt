package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.KataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(kata: KataEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(vararg entities: KataEntity)

    @Query("SELECT * FROM Kata")
    fun getKatas(): Flow<List<KataEntity>>

    @Query("SELECT * FROM Kata WHERE _id = :id")
    fun getKataById(id: String): KataEntity

    @Query("SELECT * FROM Kata WHERE faixa = :id")
    fun getKataByFaixa(id: String): Flow<List<KataEntity>>
}