package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.DefesaArmaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DefesaArmaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(defesaArma: DefesaArmaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(vararg entities: DefesaArmaEntity)

    @Query("SELECT * FROM DefesasArmas")
    fun getDefesasArmas(): Flow<List<DefesaArmaEntity>>

    @Query("SELECT * FROM DefesasArmas WHERE _id = :id")
    fun getDefesasArmasById(id: String): DefesaArmaEntity

    @Query("SELECT * FROM DefesasArmas WHERE faixa = :id")
    fun getDefesasArmasByFaixa(id: String): Flow<List<DefesaArmaEntity>>
}
