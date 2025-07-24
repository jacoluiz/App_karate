package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.EventoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventoDao {

    @Query("DELETE FROM Evento") // ou o nome real da tabela
    suspend fun limparTodos()

    @Query("SELECT * FROM Evento ORDER BY dataInicio")
    fun getEventos(): Flow<List<EventoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarTodos(vararg eventos: EventoEntity)

    @Query("SELECT * FROM Evento WHERE _id = :id")
    suspend fun getEventoPorId(id: String): EventoEntity?

    @Query("DELETE FROM Evento WHERE _id = :id")
    suspend fun deletarPorId(id: String)

}
