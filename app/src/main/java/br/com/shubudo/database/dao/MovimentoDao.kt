package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.shubudo.database.entities.MovimentoEntity

@Dao
interface MovimentoDao {

    @Query("SELECT * FROM Movimento")
    fun getMovimentos(): List<MovimentoEntity>

    @Query("SELECT * FROM Movimento WHERE id = :id")
    fun getMovimentoById(id: String): MovimentoEntity

    @Query("SELECT * FROM Movimento WHERE faixaCorresponde = :idFaixa")
    fun getMovimentosByFaixa(idFaixa: String): List<MovimentoEntity>

    @Query("SELECT * FROM Movimento WHERE faixaCorresponde = :idFaixa AND tipoMovimento = :tipo")
    fun getMovimentosByFaixaETipo(idFaixa: String, tipo: String): List<MovimentoEntity>
}