package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.DefesaPessoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DefesaPessoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(defesaPesoal: DefesaPessoalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(vararg entities: DefesaPessoalEntity)

    @Query("SELECT * FROM DefesasPessoais")
    fun getDefesasPessoais(): Flow<List<DefesaPessoalEntity>>

    @Query("SELECT * FROM DefesasPessoais WHERE _id = :id")
    fun getDefesasPessoaisById(id: String): DefesaPessoalEntity

    @Query("SELECT * FROM DefesasPessoais WHERE faixa = :id")
    fun getDefesasPessoaisByFaixa(id: String): Flow<List<DefesaPessoalEntity>>
}