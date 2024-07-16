package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.FaixaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FaixaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salvarFaixa(faixa: FaixaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(vararg entities: FaixaEntity)

    @Query("SELECT * FROM Faixa")
    fun getFaixas(): Flow<List<FaixaEntity>>

    @Query("SELECT * FROM Faixa WHERE _id = :id")
    fun getFaixaById(id: String): Flow<FaixaEntity>

    @Query("SELECT * FROM Faixa WHERE faixa = :cor")
    fun getFaixaByCor(cor: String): Flow<FaixaEntity>
}