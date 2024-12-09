package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.UsuarioDao
import br.com.shubudo.database.entities.toUsuario
import br.com.shubudo.model.Usuario
import br.com.shubudo.network.services.UsuarioService
import br.com.shubudo.network.services.toUsuario
import br.com.shubudo.network.services.toUsuarioEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val service: UsuarioService,
    private val dao: UsuarioDao
) {
    suspend fun getUsuario(): Flow<Usuario?> {
        // Retorna o usuário salvo localmente como Flow
        return dao.obterUsuarioLogado().map { it?.toUsuario() }
    }

    suspend fun login(username: String, password: String): Usuario? {
        return try {
            val credentials = mapOf(
                "username" to username,
                "senha" to password
            )
            val response = service.login(credentials)
            val entity = response.usuario.toUsuarioEntity()
            Log.i("UsuarioRepository", "login: $entity")

            // Limpa a tabela antes de salvar o novo usuário
            dao.deletarTodos()
            dao.salvarUsuario(entity)

            response.usuario.toUsuario() // Retorna o usuário para ser usado pelo ViewModel
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Erro no login: ${e.message}", e)
            null
        }
    }
}
