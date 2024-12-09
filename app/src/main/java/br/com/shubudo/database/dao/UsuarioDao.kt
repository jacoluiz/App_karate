package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuario LIMIT 1")
    fun obterUsuarioLogado(): Flow<UsuarioEntity?>

    @Query("DELETE FROM usuario") // Substitua "usuario" pelo nome da sua tabela
    suspend fun deletarTodos()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarUsuario(usuario: UsuarioEntity)
}
