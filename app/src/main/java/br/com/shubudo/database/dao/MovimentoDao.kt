package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.MovimentoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimentoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(movimento: MovimentoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(vararg entities: MovimentoEntity)

    @Query("SELECT * FROM Movimento")
    fun getMovimentos(): Flow<List<MovimentoEntity>>

    @Query("SELECT * FROM Movimento WHERE _id = :id")
    fun getMovimentoById(id: String): MovimentoEntity

    @Query("SELECT * FROM Movimento WHERE faixa = :idFaixa")
    fun getMovimentosByFaixa(idFaixa: String): Flow<List<MovimentoEntity>>

    @Query("SELECT * FROM Movimento WHERE faixa = :idFaixa AND tipoMovimento = :tipo")
    fun getMovimentosByFaixaETipo(idFaixa: String, tipo: String): List<MovimentoEntity>
}