package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.TecnicaChaoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TecnicaChaoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(tecnica: TecnicaChaoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(vararg entities: TecnicaChaoEntity)

    @Query("SELECT * FROM TecnicasChao")
    fun getTecnicasChao(): Flow<List<TecnicaChaoEntity>>

    @Query("SELECT * FROM TecnicasChao WHERE _id = :id")
    fun getTecnicaChaoById(id: String): TecnicaChaoEntity

    @Query("SELECT * FROM TecnicasChao WHERE faixa = :id")
    fun getTecnicasChaoByFaixa(id: String): Flow<List<TecnicaChaoEntity>>
}
