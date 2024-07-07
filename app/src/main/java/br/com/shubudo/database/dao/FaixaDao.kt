package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.FaixaEntity

@Dao
interface FaixaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salvarFaixa(faixa: FaixaEntity)

    @Query("SELECT * FROM Faixa")
    fun getFaixas(): List<FaixaEntity>

    @Query("SELECT * FROM Faixa WHERE id = :id")
    fun getFaixaById(id: String): FaixaEntity

    @Query("SELECT * FROM Faixa WHERE faixa = :cor")
    fun getFaixaByCor(cor: String): FaixaEntity
}