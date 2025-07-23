package br.com.shubudo.auth

import android.content.Context
import android.util.Log
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler
import com.amazonaws.regions.Regions

class CognitoAuthManager(context: Context) {

    var forgotPasswordContinuation: ForgotPasswordContinuation? = null
    var currentUsername: String? = null

    val userPool = CognitoUserPool(
        context,
        "us-east-2_TmGAXLEPO", // Substitua pelo seu User Pool ID
        "425plr8mllv4i4khs1q8s5lepl", // Substitua pelo seu App Client ID
        null, // Só preencha se você tiver App Client Secret
        Regions.US_EAST_2
    )

    fun signIn(username: String, password: String, handler: AuthenticationHandler) {
        try {
            Log.d("CognitoAuthManager", "Iniciando signIn para: $username")
            val user = userPool.getUser(username)

            // Fazer logout primeiro para limpar qualquer sessão anterior
            user.signOut()

            // Iniciar autenticação
            user.getSessionInBackground(handler)
            Log.d("CognitoAuthManager", "getSessionInBackground chamado")

        } catch (e: Exception) {
            Log.e("CognitoAuthManager", "Erro no signIn: ${e.message}", e)
            handler.onFailure(e)
        }
    }

    fun signUp(username: String, password: String, email: String, handler: SignUpHandler) {
        val attributes = CognitoUserAttributes()
        attributes.addAttribute("email", email)
        userPool.signUpInBackground(username, password, attributes, null, handler)
    }

    fun forgotPassword(username: String, handler: ForgotPasswordHandler) {
        currentUsername = username
        val user = userPool.getUser(username)
        user.forgotPasswordInBackground(object : ForgotPasswordHandler {
            override fun onSuccess() {
                Log.d("CognitoAuthManager", "Senha redefinida diretamente (caso raro)")
                handler.onSuccess()
            }

            override fun getResetCode(continuation: ForgotPasswordContinuation?) {
                forgotPasswordContinuation = continuation
                Log.d("CognitoAuthManager", "Código de verificação enviado com sucesso")
                handler.getResetCode(continuation)
            }

            override fun onFailure(exception: Exception?) {
                Log.e("CognitoAuthManager", "Erro ao solicitar código: ${exception?.message}", exception)
                handler.onFailure(exception)
            }
        })
    }

    fun confirmPassword(code: String, novaSenha: String, callback: (Boolean, String?) -> Unit) {
        try {
            Log.d("CognitoAuthManager", "Iniciando confirmPassword com código: $code")

            currentUsername?.let { username ->
                val user = userPool.getUser(username)
                user.confirmPasswordInBackground(code, novaSenha, object : ForgotPasswordHandler {
                    override fun onSuccess() {
                        Log.d("CognitoAuthManager", "Senha alterada com sucesso")
                        // Limpar dados após sucesso
                        clearResetSession()
                        callback(true, null)
                    }

                    override fun getResetCode(continuation: ForgotPasswordContinuation?) {
                        // Não usado na confirmação
                    }

                    override fun onFailure(exception: Exception?) {
                        Log.e("CognitoAuthManager", "Erro na confirmação: ${exception?.message}", exception)
                        val errorMessage = when {
                            exception?.message?.contains("CodeMismatchException", ignoreCase = true) == true ||
                                    exception?.message?.contains("Invalid verification code", ignoreCase = true) == true ->
                                "Código de verificação inválido. Tente novamente."

                            exception?.message?.contains("InvalidPasswordException", ignoreCase = true) == true ||
                                    exception?.message?.contains("password", ignoreCase = true) == true ->
                                "A senha deve ter pelo menos 8 caracteres, incluindo maiúsculas, minúsculas e números."

                            exception?.message?.contains("ExpiredCodeException", ignoreCase = true) == true ||
                                    exception?.message?.contains("expired", ignoreCase = true) == true ->
                                "Código expirado. Solicite um novo código."

                            else -> "Erro ao alterar senha: ${exception?.localizedMessage ?: "Tente novamente"}"
                        }
                        callback(false, errorMessage)
                    }
                })
            } ?: callback(false, "Username não encontrado. Reinicie o fluxo.")
        } catch (e: Exception) {
            Log.e("CognitoAuthManager", "Erro inesperado: ${e.message}", e)
            callback(false, "Erro inesperado: ${e.localizedMessage ?: "Tente novamente"}")
        }
    }

    fun reenviarCodigoConfirmacao(email: String, callback: (Boolean, String?) -> Unit) {
        val user = userPool.getUser(email)
        user.resendConfirmationCodeInBackground(object : VerificationHandler {
            override fun onSuccess(verificationCodeDeliveryMedium: CognitoUserCodeDeliveryDetails?) {
                callback(true, null)
            }

            override fun onFailure(exception: Exception?) {
                callback(false, exception?.localizedMessage ?: "Erro ao reenviar o código")
            }
        })
    }

    fun confirmCode(username: String, code: String, callback: (Boolean, String?) -> Unit) {
        val user = userPool.getUser(username)
        user.confirmSignUpInBackground(code, false, object : GenericHandler {
            override fun onSuccess() {
                callback(true, null)
            }

            override fun onFailure(exception: Exception?) {
                callback(false, exception?.message)
            }
        })
    }

    // Método para validar se temos uma sessão ativa de reset
    fun hasActiveResetSession(): Boolean {
        return currentUsername != null
    }

    // Método para limpar a sessão de reset
    fun clearResetSession() {
        forgotPasswordContinuation = null
        currentUsername = null
    }
}