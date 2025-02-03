package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.database.dao.UsuarioDao
import br.com.shubudo.database.entities.toUsuario
import br.com.shubudo.model.Usuario
import br.com.shubudo.model.toUsuarioEntity
import br.com.shubudo.network.services.UsuarioService
import br.com.shubudo.network.services.toUsuario
import br.com.shubudo.network.services.toUsuarioEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val service: UsuarioService,
    private val dao: UsuarioDao
) {

    /**
     * Retorna o usuário salvo localmente como Flow.
     * Pode ser usado para observar mudanças em um usuário logado localmente.
     */
    suspend fun getUsuario(): Flow<Usuario?> {
        return dao.obterUsuarioLogado().map { it?.toUsuario() }
    }

    /**
     * Faz login enviando tanto "username" quanto "email" para o back-end.
     */
    suspend fun login(userInput: String, password: String): Usuario? {
        return try {
            val credentials = if (userInput.contains("@")) {
                mapOf("email" to userInput, "senha" to password)
            } else {
                mapOf("username" to userInput, "senha" to password)
            }

            // Chama o serviço de login
            val response = service.login(credentials)

            // Converte a resposta em Entity para salvar no banco local
            val entity = response.usuario.toUsuarioEntity()

            // Limpa a tabela antes de salvar o novo usuário logado
            dao.deletarTodos()
            dao.salvarUsuario(entity)

            // Retorna o usuário para o ViewModel
            response.usuario.toUsuario()
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Erro no login: ${e.message}", e)
            null
        }
    }

    /**
     * Cadastra um novo usuário chamando o endpoint do serviço.
     */
    suspend fun cadastrarUsuario(usuario: Usuario): Usuario? {
        return try {
            // Envia o usuário para o serviço (criar usuário no backend)
            val response = service.criarUsuarios(usuario)

            // Converte para entidade para salvar localmente
            val usuarioEntity = usuario.toUsuarioEntity()
            if (usuarioEntity != null) {
                dao.salvarUsuario(usuarioEntity)
            }

            // Retorna o usuário cadastrado (mapeado a partir da resposta do servidor)
            response.toUsuario()
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Erro no cadastro: ${e.message}", e)
            null
        }
    }

    /**
     * Atualiza um usuário no backend (rota PUT /usuarios/:id) e reflete as mudanças no banco local.
     */
    suspend fun atualizarUsuario(usuario: Usuario): Usuario? {
        return try {
            // 1. Pega o usuário logado localmente (o Flow retorna o primeiro valor ou null se não existir)
            val localUserEntity = dao.obterUsuarioLogado().firstOrNull()
            if (localUserEntity == null) {
                // Se não há usuário salvo, não podemos atualizar
                Log.e("UsuarioRepository", "Nenhum usuário logado localmente para atualizar.")
                return null
            }

            // 2. Usa o _id do usuário local para fazer a chamada ao serviço
            val userId = localUserEntity._id  // ajuste o nome do campo, se necessário
            if (userId.isBlank()) {
                // Se, por algum motivo, o ID estiver nulo ou vazio, não prosseguir
                Log.e("UsuarioRepository", "O usuário local não possui um ID válido.")
                return null
            }

            val userToUpdate = usuario.copy(
                _id = userId,
                senha = localUserEntity.senha
            )
            // 4. Chama o serviço remotor
            val responseDto = service.atualizarUsuario(userId, userToUpdate)

            responseDto?.copy(
                senha = localUserEntity.senha
            )

            // 5. Atualiza o usuário local (caso queira refletir as mudanças offline)
            responseDto?.let { updatedDto ->
                val updatedEntity = updatedDto.toUsuarioEntity()
                dao.atualizarUsuario(updatedEntity)
            }

            // 6. Converte a resposta para o modelo de domínio e retorna
            responseDto?.toUsuario()
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Erro ao atualizar usuário: ${e.message}", e)
            null
        }
    }

}
