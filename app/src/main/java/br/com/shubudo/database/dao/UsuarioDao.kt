package br.com.shubudo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.shubudo.database.entities.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salvarUsuario(usuario: UsuarioEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(vararg usuarios: UsuarioEntity)

    @Query("SELECT * FROM Usuario LIMIT 1")
    fun getUsuario(): Flow<UsuarioEntity?>

    @Query("DELETE FROM Usuario")
    suspend fun clear()

    @Query("SELECT * FROM Usuario")
    fun getUsuarios(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM Usuario WHERE _id = :id")
    fun getUsuarioById(id: String): Flow<UsuarioEntity>

    @Query("SELECT * FROM Usuario WHERE username = :username")
    fun getUsuarioByUsername(username: String): Flow<UsuarioEntity>

    @Query("SELECT * FROM Usuario WHERE email = :email")
    fun getUsuarioByEmail(email: String): Flow<UsuarioEntity>

    @Query("SELECT * FROM Usuario WHERE corFaixa = :corFaixa")
    fun getUsuariosByFaixa(corFaixa: String): Flow<List<UsuarioEntity>>
}
