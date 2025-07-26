package br.com.shubudo.repositories

import android.content.Context
import android.util.Log
import br.com.shubudo.auth.CognitoAuthManager
import br.com.shubudo.database.dao.UsuarioDao
import br.com.shubudo.database.entities.toUsuario
import br.com.shubudo.model.Usuario
import br.com.shubudo.model.toUsuarioEntity
import br.com.shubudo.network.services.UsuarioService
import br.com.shubudo.network.services.toUsuario
import br.com.shubudo.utils.getFcmToken
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
import kotlinx.coroutines.flow.flowOf
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

    suspend fun getUsuarios() = flowOf(
        try {
            service.getUsuarios().map { it.toUsuario() }
        } catch (e: Exception) {
            emptyList()
        }
    )

    suspend fun getUsuarioPorId(id: String): Usuario? = withContext(Dispatchers.IO) {
        return@withContext try {
            service.getUsuariosPorId(id).toUsuario()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun login(context: Context, userInput: String, password: String): Usuario =
        withContext(Dispatchers.IO) {
            withTimeoutOrNull(30000) {
                suspendCancellableCoroutine { cont ->
                    val usernameSafe = userInput.trim()
                    val authHandler = object : AuthenticationHandler {
                        override fun onSuccess(
                            userSession: CognitoUserSession?,
                            newDevice: CognitoDevice?
                        ) {
                            if (userSession == null || !userSession.isValid) {
                                if (cont.isActive) cont.resume(null, onCancellation = null)
                                return
                            }

                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val usuarios = service.getUsuarios().map { it.toUsuario() }
                                    val fcmToken = getFcmToken(context)

                                    val usuario = usuarios.find {
                                        it.email.equals(userInput, true) || it.username.equals(
                                            userInput,
                                            true
                                        )
                                    }

                                    if (usuario != null) {
                                        // Atualiza com token
                                        val usuarioComToken = usuario.copy(fcmToken = fcmToken)
                                        usuario._id?.let {
                                            service.atualizarUsuario(
                                                it,
                                                usuarioComToken
                                            )
                                        }

                                        dao.deletarTodos()
                                        usuarioComToken.toUsuarioEntity()
                                            ?.let { dao.salvarUsuario(it) }
                                        if (cont.isActive) cont.resume(
                                            usuarioComToken,
                                            onCancellation = null
                                        )
                                    }


                                    if (usuario != null) {
                                        dao.deletarTodos()
                                        usuario.toUsuarioEntity()?.let { dao.salvarUsuario(it) }
                                        if (cont.isActive) cont.resume(
                                            usuario,
                                            onCancellation = null
                                        )
                                    } else {
                                        if (cont.isActive) cont.resumeWithException(Exception("Usuário não existe na API"))
                                    }
                                } catch (e: Exception) {
                                    if (cont.isActive) cont.resumeWithException(e)
                                }
                            }
                        }

                        override fun onFailure(exception: Exception?) {
                            if (cont.isActive) {
                                when (exception) {
                                    is UserNotConfirmedException -> cont.resumeWithException(
                                        exception
                                    )

                                    else -> cont.resumeWithException(
                                        exception
                                            ?: Exception("Falha desconhecida no login Cognito")
                                    )
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
                                if (cont.isActive) cont.resumeWithException(Exception("AuthenticationContinuation é null"))
                            }
                        }

                        override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                            if (cont.isActive) cont.resumeWithException(Exception("MFA não suportado"))
                        }

                        override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                            if (cont.isActive) cont.resumeWithException(Exception("Desafio de autenticação não suportado"))
                        }
                    }

                    try {
                        cognito.signIn(usernameSafe, password, authHandler)
                    } catch (e: Exception) {
                        if (cont.isActive) cont.resumeWithException(e)
                    }
                }
            } ?: throw Exception("Timeout no login")
        }

    suspend fun getCorFaixaLocal(userInput: String): String? = withContext(Dispatchers.IO) {
        val usuario = dao.getUsuarioByEmailOuUsername(userInput.trim())
        return@withContext usuario?.corFaixa?.takeIf { it.isNotBlank() }
    }

    suspend fun cadastrarUsuario(context: Context, usuario: Usuario, senha: String): Usuario? =
        withContext(Dispatchers.IO) {
            val result = CompletableDeferred<Usuario?>()
            val fcmToken = getFcmToken(context)
            val usuarioComToken = usuario.copy(fcmToken = fcmToken)

            cognito.signUp(usuario.username, senha, usuario.email, object : SignUpHandler {
                override fun onSuccess(user: CognitoUser?, signUpResult: SignUpResult?) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val usuarioSalvo = service.criarUsuarios(usuarioComToken)
                            result.complete(usuarioSalvo.toUsuario())
                        } catch (e: Exception) {
                            result.complete(usuarioComToken)
                        }
                    }
                }

                override fun onFailure(exception: Exception?) {
                    exception?.message.orEmpty()
                    result.complete(null)
                }
            })

            result.await()
        }

    suspend fun atualizarUsuario(context: Context, usuario: Usuario): Usuario? =
        withContext(Dispatchers.IO) {
            try {
                val localUserEntity = dao.obterUsuarioLogado().firstOrNull()
                if (localUserEntity == null || localUserEntity._id.isBlank()) return@withContext null

                val usuariosResponse = service.getUsuarios()
                val usuarios = usuariosResponse.map { it.toUsuario() }

                val outrosUsuarios = usuarios.filter { it._id != localUserEntity._id }
                if (outrosUsuarios.any {
                        it.email.equals(
                            usuario.email,
                            ignoreCase = true
                        )
                    }) return@withContext null
                if (outrosUsuarios.any {
                        it.username.equals(
                            usuario.username,
                            ignoreCase = true
                        )
                    }) return@withContext null

                val fcmToken = getFcmToken(context)
                val userToUpdate = usuario.copy(_id = localUserEntity._id, fcmToken = fcmToken)

                service.atualizarUsuario(localUserEntity._id, userToUpdate)?.let {
                    userToUpdate.toUsuarioEntity()?.let { dao.atualizarUsuario(it) }
                    return@withContext userToUpdate
                } ?: return@withContext null
            } catch (ce: CancellationException) {
                throw ce
            } catch (e: Exception) {
                return@withContext null
            }
        }

    suspend fun atualizarUsuarioAdm(context: Context, usuario: Usuario): Usuario? =
        withContext(Dispatchers.IO) {
            try {
                val localUserEntity = dao.obterUsuarioLogado().firstOrNull()
                if (localUserEntity == null || localUserEntity._id.isBlank()) return@withContext null

                val usuariosResponse = service.getUsuarios()
                val usuarios = usuariosResponse.map { it.toUsuario() }

                // Ignora o usuário que está sendo atualizado na verificação de duplicidade
                val outrosUsuarios = usuarios.filter { it._id != usuario._id }

                if (outrosUsuarios.any {
                        it.email.equals(usuario.email, ignoreCase = true)
                    }) return@withContext null
                if (outrosUsuarios.any {
                        it.username.equals(usuario.username, ignoreCase = true)
                    }) return@withContext null

                val fcmToken = getFcmToken(context)
                val userToUpdate = usuario.copy(fcmToken = fcmToken)

                return@withContext service.atualizarUsuario(
                    usuario._id ?: return@withContext null,
                    userToUpdate
                )
                    ?.toUsuario()
            } catch (ce: CancellationException) {
                throw ce
            } catch (e: Exception) {
                return@withContext null
            }
        }

    suspend fun logout() = withContext(Dispatchers.IO) {
        try {
            cognito.userPool.currentUser?.signOut()
            dao.limparUsuario()
        } catch (e: Exception) {
            dao.limparUsuario()
        }
    }
}
