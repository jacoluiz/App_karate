package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.shubudo.database.entities.KataEntity

@Dao
interface KataDao {

    @Query("SELECT * FROM Kata")
    fun getKatas(): List<KataEntity>

    @Query("SELECT * FROM Kata WHERE id = :id")
    fun getKataById(id: String): KataEntity
}