package br.com.shubudo.database.dao

import androidx.room.*
import br.com.shubudo.database.entities.GaleriaEventoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GaleriaEventoDao {

    @Query("SELECT * FROM GaleriaEvento ORDER BY data DESC")
    fun listarEventos(): Flow<List<GaleriaEventoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarEventos(eventos: List<GaleriaEventoEntity>)

    @Query("DELETE FROM GaleriaEvento")
    suspend fun deletarTodos()

    @Query("SELECT * FROM GaleriaEvento WHERE _id = :id LIMIT 1")
    suspend fun obterEventoPorId(id: String): GaleriaEventoEntity?
}
