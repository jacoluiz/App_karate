package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.shubudo.database.entities.DefesaPessoalEntity

@Dao
interface DefesaPessoalDao {

    @Query("SELECT * FROM DefesasPessoais")
    fun getDefesasPessoais(): List<DefesaPessoalEntity>

    @Query("SELECT * FROM DefesasPessoais WHERE id = :id")
    fun getDefesasPessoaisById(id: String): DefesaPessoalEntity
}