package br.com.shubudo.auth

import android.content.Context
import android.util.Log
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.regions.Regions

class CognitoAuthManager(context: Context) {

    var forgotPasswordContinuation: ForgotPasswordContinuation? = null

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

            // Criar AuthenticationDetails
            val authDetails = AuthenticationDetails(username, password, null)
            Log.d("CognitoAuthManager", "AuthenticationDetails criado")

            // Iniciar autenticação
            user.initiateUserAuthentication(authDetails, handler, true).run()
            Log.d("CognitoAuthManager", "initiateUserAuthentication chamado")

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
        val user = userPool.getUser(username)
        user.forgotPassword(handler)
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
}