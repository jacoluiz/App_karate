package br.com.shubudo.repositories

import android.util.Log
import br.com.shubudo.auth.CognitoAuthManager
import br.com.shubudo.database.dao.UsuarioDao
import br.com.shubudo.database.entities.toUsuario
import br.com.shubudo.model.Usuario
import br.com.shubudo.model.toUsuarioEntity
import br.com.shubudo.network.services.UsuarioService
import br.com.shubudo.network.services.toUsuario
import br.com.shubudo.network.services.toUsuarioEntity
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
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

    suspend fun login(userInput: String, password: String): Usuario? = withContext(Dispatchers.IO) {
        // Timeout de 30 segundos para evitar travamento
        withTimeoutOrNull(30000) {
            suspendCancellableCoroutine { cont ->
                val usernameSafe = userInput.trim()
                val passwordSafe = password

                Log.i("UsuarioRepository", "Login começou para usuário: $usernameSafe")

                val authHandler = object : AuthenticationHandler {
                    override fun onSuccess(
                        userSession: CognitoUserSession?,
                        newDevice: CognitoDevice?
                    ) {
                        Log.i("UsuarioRepository", "✅ Login Cognito bem-sucedido!")

                        if (userSession == null || !userSession.isValid) {
                            Log.e("UsuarioRepository", "❌ Sessão inválida do Cognito")
                            if (cont.isActive) {
                                cont.resume(null, onCancellation = null)
                            }
                            return
                        }

                        Log.i("UsuarioRepository", "🔍 Buscando usuário na API...")
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val usuarios = service.getUsuarios().map { it.toUsuario() }
                                val usuario = usuarios.find {
                                    it.email.equals(userInput, true) || it.username.equals(userInput, true)
                                }

                                if (usuario != null) {
                                    Log.i("UsuarioRepository", "✅ Usuário encontrado: ${usuario.username}")
                                    dao.deletarTodos()
                                    usuario.toUsuarioEntity()?.let { dao.salvarUsuario(it) }
                                    if (cont.isActive) {
                                        cont.resume(usuario, onCancellation = null)
                                    }
                                } else {
                                    Log.e("UsuarioRepository", "❌ Usuário não encontrado na API")
                                    if (cont.isActive) {
                                        cont.resumeWithException(Exception("Usuário não existe na API"))
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("UsuarioRepository", "❌ Erro ao buscar usuário na API: ${e.message}")
                                if (cont.isActive) {
                                    cont.resumeWithException(e)
                                }
                            }
                        }
                    }

                    override fun onFailure(exception: Exception?) {
                        Log.e("UsuarioRepository", "❌ Erro no login Cognito: ${exception?.message}", exception)
                        if (cont.isActive) {
                            cont.resumeWithException(
                                exception ?: Exception("Falha desconhecida no login Cognito")
                            )
                        }
                    }

                    override fun getAuthenticationDetails(
                        authenticationContinuation: AuthenticationContinuation?,
                        userId: String?
                    ) {
                        Log.i("UsuarioRepository", "🔄 getAuthenticationDetails chamado para: $userId")

                        if (authenticationContinuation != null) {
                            val authDetails = AuthenticationDetails(usernameSafe, passwordSafe, null)
                            authenticationContinuation.setAuthenticationDetails(authDetails)
                            authenticationContinuation.continueTask()
                            Log.i("UsuarioRepository", "🔄 AuthenticationDetails fornecido e continuando...")
                        } else {
                            Log.e("UsuarioRepository", "❌ AuthenticationContinuation é null")
                            if (cont.isActive) {
                                cont.resumeWithException(Exception("AuthenticationContinuation é null"))
                            }
                        }
                    }

                    override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                        Log.w("UsuarioRepository", "⚠️ MFA requerido, não implementado")
                        if (cont.isActive) {
                            cont.resumeWithException(Exception("MFA não suportado"))
                        }
                    }

                    override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                        Log.w("UsuarioRepository", "⚠️ Desafio adicional não implementado")
                        if (cont.isActive) {
                            cont.resumeWithException(Exception("Desafio de autenticação não suportado"))
                        }
                    }
                }

                try {
                    Log.i("UsuarioRepository", "🚀 Iniciando signIn no Cognito...")
                    cognito.signIn(usernameSafe, passwordSafe, authHandler)
                } catch (e: Exception) {
                    Log.e("UsuarioRepository", "❌ Erro ao iniciar signIn: ${e.message}", e)
                    if (cont.isActive) {
                        cont.resumeWithException(e)
                    }
                }
            }
        } ?: run {
            Log.e("UsuarioRepository", "⏰ Timeout no login - operação cancelada")
            throw Exception("Timeout no login")
        }
    }

    @OptIn(InternalCoroutinesApi::class)
    suspend fun iniciarEsqueciSenha(email: String): Boolean = withContext(Dispatchers.IO) {
        val user = cognito.userPool.getUser(email.trim())

        suspendCancellableCoroutine { cont ->
            user.forgotPasswordInBackground(object : ForgotPasswordHandler {
                override fun onSuccess() {
                    cont.tryResume(true)?.let { token -> cont.completeResume(token) }
                }

                override fun getResetCode(continuation: ForgotPasswordContinuation) {
                    cognito.forgotPasswordContinuation = continuation
                    cont.tryResume(true)?.let { token -> cont.completeResume(token) }
                }

                override fun onFailure(exception: Exception) {
                    Log.e("UsuarioRepository", "Erro forgotPassword: ${exception.message}", exception)
                    cont.tryResume(false)?.let { token -> cont.completeResume(token) }
                }
            })
        }
    }

    fun confirmarNovaSenha(codigo: String, novaSenha: String): Boolean {
        return try {
            cognito.forgotPasswordContinuation?.apply {
                setPassword(novaSenha)
                setVerificationCode(codigo)
                continueTask()
            }
            true
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Erro confirmando nova senha: ${e.message}")
            false
        }
    }

    suspend fun cadastrarUsuario(usuario: Usuario): Usuario? = withContext(Dispatchers.IO) {
        val result = CompletableDeferred<Usuario?>()

        cognito.signUp(usuario.username, usuario.senha, usuario.email, object : SignUpHandler {
            override fun onSuccess(user: CognitoUser?, signUpResult: SignUpResult?) {
                CoroutineScope(Dispatchers.IO).launch {
                    val usuarioParaApi = usuario.copy(senha = "")
                    try {
                        val usuarioSalvo = service.criarUsuarios(usuarioParaApi)
                        usuarioSalvo.toUsuarioEntity().let { dao.salvarUsuario(it) }
                        result.complete(usuarioSalvo.toUsuario())
                    } catch (e: Exception) {
                        Log.e("UsuarioRepository", "Erro ao enviar para API: ${e.message}")
                        result.complete(usuario)
                    }
                }
            }

            override fun onFailure(exception: Exception?) {
                val msg = exception?.message.orEmpty()
                Log.e("UsuarioRepository", "Erro no cadastro Cognito: $msg")
                if (msg.contains("User already exists", ignoreCase = true)) {
                    Log.w("UsuarioRepository", "Usuário já existe. Pode estar pendente de confirmação.")
                    result.complete(usuario)
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
            Log.e("UsuarioRepository", "Erro no logout: ${e.message}")
            // Limpar dados locais mesmo se houver erro no logout do Cognito
            dao.limparUsuario()
        }
    }

    suspend fun atualizarUsuario(usuario: Usuario): Usuario? = withContext(Dispatchers.IO) {
        try {
            val localUserEntity = dao.obterUsuarioLogado().firstOrNull()
            if (localUserEntity == null || localUserEntity._id.isBlank()) {
                Log.e("UsuarioRepository", "Usuário local inválido.")
                return@withContext null
            }

            val usuariosResponse = service.getUsuarios()
            val usuarios = usuariosResponse.map { it.toUsuario() }

            val outrosUsuarios = usuarios.filter { it._id != localUserEntity._id }
            if (outrosUsuarios.any { it.email.equals(usuario.email, ignoreCase = true) }) {
                Log.e("UsuarioRepository", "Email duplicado.")
                return@withContext null
            }
            if (outrosUsuarios.any { it.username.equals(usuario.username, ignoreCase = true) }) {
                Log.e("UsuarioRepository", "Username duplicado.")
                return@withContext null
            }

            val userToUpdate = usuario.copy(_id = localUserEntity._id, senha = localUserEntity.senha)
            service.atualizarUsuario(localUserEntity._id, userToUpdate)?.let {
                userToUpdate.toUsuarioEntity()?.let { dao.atualizarUsuario(it) }
                return@withContext userToUpdate
            } ?: run {
                Log.e("UsuarioRepository", "Falha ao atualizar o perfil na API.")
                return@withContext null
            }
        } catch (ce: CancellationException) {
            Log.e("UsuarioRepository", "Operação cancelada", ce)
            throw ce
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Erro ao atualizar usuário", e)
            null
        }
    }
}