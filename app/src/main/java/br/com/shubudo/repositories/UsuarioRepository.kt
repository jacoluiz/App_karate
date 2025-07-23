package br.com.shubudo.repositories

import br.com.shubudo.auth.CognitoAuthManager
import br.com.shubudo.database.dao.UsuarioDao
import br.com.shubudo.database.entities.toUsuario
import br.com.shubudo.model.Usuario
import br.com.shubudo.model.toUsuarioEntity
import br.com.shubudo.network.services.UsuarioService
import br.com.shubudo.network.services.toUsuario
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import com.amazonaws.services.cognitoidentityprovider.model.UserNotConfirmedException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class UsuarioRepository @Inject constructor(
    private val service: UsuarioService,
    private val dao: UsuarioDao,
    private val cognito: CognitoAuthManager
) {

    fun getUsuario() = dao.obterUsuarioLogado().map { it?.toUsuario() }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun login(userInput: String, password: String): Usuario = withContext(Dispatchers.IO) {
        withTimeoutOrNull(30000) {
            suspendCancellableCoroutine { cont ->
                val usernameSafe = userInput.trim()
                val authHandler = object : AuthenticationHandler {
                    override fun onSuccess(
                        userSession: CognitoUserSession?,
                        newDevice: CognitoDevice?
                    ) {
                        if (userSession == null || !userSession.isValid) {
                            if (cont.isActive) {
                                cont.resume(null, onCancellation = null)
                            }
                            return
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val usuarios = service.getUsuarios().map { it.toUsuario() }
                                val usuario = usuarios.find {
                                    it.email.equals(
                                        userInput,
                                        true
                                    ) || it.username.equals(userInput, true)
                                }

                                if (usuario != null) {
                                    dao.deletarTodos()
                                    usuario.toUsuarioEntity()?.let { dao.salvarUsuario(it) }
                                    if (cont.isActive) {
                                        cont.resume(usuario, onCancellation = null)
                                    }
                                } else {
                                    if (cont.isActive) {
                                        cont.resumeWithException(Exception("Usuário não existe na API"))
                                    }
                                }
                            } catch (e: Exception) {
                                if (cont.isActive) {
                                    cont.resumeWithException(e)
                                }
                            }
                        }
                    }

                    override fun onFailure(exception: Exception?) {
                        if (cont.isActive) {
                            // Tratar UserNotConfirmedException de forma especial
                            when (exception) {
                                is UserNotConfirmedException -> {
                                    // Propagar diretamente a exceção UserNotConfirmedException
                                    cont.resumeWithException(exception)
                                }
                                else -> {
                                    // Para outras exceções, propagar normalmente
                                    cont.resumeWithException(
                                        exception ?: Exception("Falha desconhecida no login Cognito")
                                    )
                                }
                            }
                        }
                    }

                    override fun getAuthenticationDetails(
                        authenticationContinuation: AuthenticationContinuation?,
                        userId: String?
                    ) {

                        if (authenticationContinuation != null) {
                            val authDetails =
                                AuthenticationDetails(usernameSafe, password, null)
                            authenticationContinuation.setAuthenticationDetails(authDetails)
                            authenticationContinuation.continueTask()
                        } else {
                            if (cont.isActive) {
                                cont.resumeWithException(Exception("AuthenticationContinuation é null"))
                            }
                        }
                    }

                    override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                        if (cont.isActive) {
                            cont.resumeWithException(Exception("MFA não suportado"))
                        }
                    }

                    override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                        if (cont.isActive) {
                            cont.resumeWithException(Exception("Desafio de autenticação não suportado"))
                        }
                    }
                }

                try {
                    cognito.signIn(usernameSafe, password, authHandler)
                } catch (e: Exception) {
                    if (cont.isActive) {
                        cont.resumeWithException(e)
                    }
                }
            }
        } ?: run {
            throw Exception("Timeout no login")
        }
    }

    suspend fun getCorFaixaLocal(userInput: String): String? = withContext(Dispatchers.IO) {
        val usuario = dao.getUsuarioByEmailOuUsername(userInput.trim())
        return@withContext usuario?.corFaixa?.takeIf { it.isNotBlank() }
    }

    suspend fun cadastrarUsuario(usuario: Usuario): Usuario? = withContext(Dispatchers.IO) {
        val result = CompletableDeferred<Usuario?>()

        cognito.signUp(usuario.username, usuario.senha, usuario.email, object : SignUpHandler {
            override fun onSuccess(user: CognitoUser?, signUpResult: SignUpResult?) {
                CoroutineScope(Dispatchers.IO).launch {
                    val usuarioParaApi = usuario.copy(senha = "")
                    try {
                        val usuarioSalvo = service.criarUsuarios(usuarioParaApi)
                        result.complete(usuarioSalvo.toUsuario())
                    } catch (e: Exception) {
                        result.complete(usuario)
                    }
                }
            }

            override fun onFailure(exception: Exception?) {
                val msg = exception?.message.orEmpty()
                if (msg.contains("User already exists", ignoreCase = true)) {
                    result.complete(null) // agora retorna nulo corretamente
                } else {
                    result.complete(null)
                }

            }
        })

        result.await()
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        try {
            // Fazer logout do Cognito também
            val currentUser = cognito.userPool.currentUser
            currentUser?.signOut()
            dao.limparUsuario()
        } catch (e: Exception) {
            // Limpar dados locais mesmo se houver erro no logout do Cognito
            dao.limparUsuario()
        }
    }

    suspend fun atualizarUsuario(usuario: Usuario): Usuario? = withContext(Dispatchers.IO) {
        try {
            val localUserEntity = dao.obterUsuarioLogado().firstOrNull()
            if (localUserEntity == null || localUserEntity._id.isBlank()) {
                return@withContext null
            }

            val usuariosResponse = service.getUsuarios()
            val usuarios = usuariosResponse.map { it.toUsuario() }

            val outrosUsuarios = usuarios.filter { it._id != localUserEntity._id }
            if (outrosUsuarios.any { it.email.equals(usuario.email, ignoreCase = true) }) {
                return@withContext null
            }
            if (outrosUsuarios.any { it.username.equals(usuario.username, ignoreCase = true) }) {
                return@withContext null
            }

            val userToUpdate =
                usuario.copy(_id = localUserEntity._id, senha = localUserEntity.senha)
            service.atualizarUsuario(localUserEntity._id, userToUpdate)?.let {
                userToUpdate.toUsuarioEntity()?.let { dao.atualizarUsuario(it) }
                return@withContext userToUpdate
            } ?: run {
                return@withContext null
            }
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Exception) {
            null
        }
    }
}