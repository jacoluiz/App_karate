package br.com.shubudo.database.dao

import androidx.room.*
import br.com.shubudo.database.entities.GaleriaFotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GaleriaFotoDao {

    @Query("SELECT * FROM GaleriaFoto WHERE eventoId = :eventoId ORDER BY createdAt DESC")
    fun listarFotosPorEvento(eventoId: String): Flow<List<GaleriaFotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarFotos(fotos: List<GaleriaFotoEntity>)

    @Query("DELETE FROM GaleriaFoto")
    suspend fun deletarTodas()

    @Query("SELECT * FROM GaleriaFoto WHERE _id = :id LIMIT 1")
    suspend fun obterFotoPorId(id: String): GaleriaFotoEntity?

    @Query("DELETE FROM GaleriaFoto WHERE eventoId = :eventoId")
    suspend fun removerPorEvento(eventoId: String)
}
