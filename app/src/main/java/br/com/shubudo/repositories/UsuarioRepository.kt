package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.UsuarioDao
import br.com.shubudo.database.entities.toUsuario
import br.com.shubudo.model.Usuario
import br.com.shubudo.model.toUsuarioEntity
import br.com.shubudo.network.services.UsuarioService
import br.com.shubudo.network.services.toUsuario
import br.com.shubudo.network.services.toUsuarioEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val service: UsuarioService,
    private val dao: UsuarioDao
) {

    suspend fun getUsuario() = dao.obterUsuarioLogado().map { it?.toUsuario() }

    suspend fun login(userInput: String, password: String): Usuario? {
        return try {
            val credentials = if (userInput.contains("@")) {
                mapOf("email" to userInput, "senha" to password)
            } else {
                mapOf("username" to userInput, "senha" to password)
            }
            val response = service.login(credentials)
            val entity = response.usuario.toUsuarioEntity()
            dao.deletarTodos()
            dao.salvarUsuario(entity)
            response.usuario.toUsuario()
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Erro no login: ${e.message}", e)
            null
        }
    }

    suspend fun cadastrarUsuario(usuario: Usuario): Usuario? {
        return try {
            val response = service.criarUsuarios(usuario)
            val usuarioEntity = usuario.toUsuarioEntity()
            if (usuarioEntity != null) {
                dao.salvarUsuario(usuarioEntity)
            }
            response.toUsuario()
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Erro no cadastro: ${e.message}", e)
            null
        }
    }

    suspend fun logout() {
        dao.limparUsuario() // ou deleteAll(), dependendo da sua DAO
    }

    /**
     * Atualiza o perfil do usuário, validando duplicidades no email e username.
     *
     * 1. Recupera o usuário logado localmente.
     * 2. Busca todos os usuários cadastrados na API para validação.
     * 3. Remove o próprio usuário da lista e valida se há duplicidade de email ou username.
     *    Se houver duplicidade, retorna null.
     * 4. Cria o objeto a ser atualizado, mantendo o ID e a senha do usuário local.
     * 5. Chama o endpoint de atualização da API para atualizar o perfil.
     * 6. Atualiza o usuário no banco de dados local.
     * 7. Retorna o usuário atualizado ou null em caso de erro.
     */
    suspend fun atualizarUsuario(usuario: Usuario): Usuario? {
        return try {
            // 1. Obtém o usuário logado localmente (contexto IO)
            val localUserEntity = withContext(Dispatchers.IO) {
                dao.obterUsuarioLogado().firstOrNull()
            }
            if (localUserEntity == null) {
                Log.e("UsuarioRepository", "Nenhum usuário logado localmente para atualizar.")
                return null
            }
            val userId = localUserEntity._id
            if (userId.isBlank()) {
                Log.e("UsuarioRepository", "O usuário local não possui um ID válido.")
                return null
            }

            // 2. Busca todos os usuários cadastrados na API para validação (contexto IO)
            val usuariosResponse = withContext(Dispatchers.IO) { service.getUsuarios() }
            val usuarios = usuariosResponse.map { it.toUsuario() }

            // 3. Remove o próprio usuário e valida duplicidade de email e username.
            val outrosUsuarios = usuarios.filter { it._id != userId }
            if (outrosUsuarios.any { it.email.equals(usuario.email, ignoreCase = true) }) {
                Log.e("UsuarioRepository", "O email '${usuario.email}' já está cadastrado para outro usuário.")
                return null
            }
            if (outrosUsuarios.any { it.username.equals(usuario.username, ignoreCase = true) }) {
                Log.e("UsuarioRepository", "O nome de usuário '${usuario.username}' já está em uso.")
                return null
            }

            // 4. Cria o objeto a ser atualizado, mantendo o ID e a senha do usuário local.
            val userToUpdate = usuario.copy(
                _id = userId,
                senha = localUserEntity.senha
            )
            // 5. Chama o endpoint de atualização da API (contexto IO)
            val responseDto = withContext(Dispatchers.IO) {
                service.atualizarUsuario(userId, userToUpdate)
            }
            if (responseDto == null) {
                Log.e("UsuarioRepository", "Falha ao atualizar o perfil na API.")
                return null
            }
            // 7. Atualiza o usuário no banco de dados local (contexto IO)

            userToUpdate.toUsuarioEntity()?.let { dao.atualizarUsuario(it) }

            userToUpdate
        } catch (ce: CancellationException) {
            Log.e("UsuarioRepository", "Operação cancelada: ${ce.message}", ce)
            throw ce
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Erro ao atualizar usuário: ${e.message}", e)
            null
        }
    }
}
