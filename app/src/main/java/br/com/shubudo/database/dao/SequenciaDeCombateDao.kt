package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.shubudo.database.entities.SequenciaDeCombateEntity

@Dao
interface SequenciaDeCombateDao {

    @Query("SELECT * FROM SequenciaDeCombate")
    fun getSequenciaDeCombate(): List<SequenciaDeCombateEntity>

    @Query("SELECT * FROM SequenciaDeCombate WHERE id = :id")
    fun getSequenciaDeCombateById(id: String): SequenciaDeCombateEntity
}