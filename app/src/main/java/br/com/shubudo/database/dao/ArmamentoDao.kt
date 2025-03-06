package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.ArmamentoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArmamentoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(armamento: ArmamentoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(vararg entities: ArmamentoEntity)

    @Query("SELECT * FROM Armamento")
    fun getArmamentos(): Flow<List<ArmamentoEntity>>

    @Query("SELECT * FROM Armamento WHERE _id = :id")
    fun getArmamentoById(id: String): ArmamentoEntity

    @Query("SELECT * FROM Armamento WHERE faixa = :faixa")
    fun getArmamentoByFaixa(faixa: String): Flow<List<ArmamentoEntity>>
}
