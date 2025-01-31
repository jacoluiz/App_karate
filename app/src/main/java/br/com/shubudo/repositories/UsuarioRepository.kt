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
     * Faz login enviando tanto "username" quanto "email" para o back-end,
     * dependendo do que o usuário digitou (se contém "@", consideramos email).
     */
    suspend fun login(userInput: String, password: String): Usuario? {
        return try {
            // Se o usuário digitou um '@', assumimos que seja email
            val credentials = if (userInput.contains("@")) {
                mapOf(
                    "email" to userInput,
                    "senha" to password
                )
            } else {
                mapOf(
                    "username" to userInput,
                    "senha" to password
                )
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
            // Mapeia o modelo para a entidade
            val usuarioEntity = usuario.toUsuarioEntity()

            // Envia o usuário para o serviço (criar usuário no backend)
            val response = service.criarUsuarios(usuario)

            // Salva o usuário localmente após o sucesso do cadastro
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
}
