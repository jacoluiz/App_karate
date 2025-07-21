package br.com.shubudo.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.auth.CognitoAuthManager
import br.com.shubudo.ui.uistate.EsqueciMinhaSenhaUiState
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EsqueciMinhaSenhaViewModel @Inject constructor(
    private val cognitoAuthManager: CognitoAuthManager
) : ViewModel() {

    var email by mutableStateOf("")
    var codigo by mutableStateOf("")
    var novaSenha by mutableStateOf("")
    var confirmarSenha by mutableStateOf("")

    private val _etapa = mutableIntStateOf(1)
    val etapa: State<Int> = _etapa

    private val _uiState = mutableStateOf<EsqueciMinhaSenhaUiState>(EsqueciMinhaSenhaUiState.Idle)
    val uiState: State<EsqueciMinhaSenhaUiState> = _uiState

    private val _showSuccessDialog = mutableStateOf(false)
    val showSuccessDialog: State<Boolean> = _showSuccessDialog
    fun setEtapa(novaEtapa: Int) {
        _etapa.intValue = novaEtapa
    }

    fun solicitarCodigo() {
        if (email.trim().isEmpty()) {
            _uiState.value = EsqueciMinhaSenhaUiState.Error("Digite um e-mail válido.")
            return
        }

        // Validação básica de formato de email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            _uiState.value = EsqueciMinhaSenhaUiState.Error("Digite um e-mail válido.")
            return
        }
        _uiState.value = EsqueciMinhaSenhaUiState.Loading

        cognitoAuthManager.forgotPassword(email.trim(), object : ForgotPasswordHandler {
            override fun onSuccess() {
                // Caso raro: redefinição sem código
                _showSuccessDialog.value = true
                _uiState.value = EsqueciMinhaSenhaUiState.Idle
            }

            override fun getResetCode(continuation: ForgotPasswordContinuation?) {
                // Código enviado com sucesso
                _uiState.value = EsqueciMinhaSenhaUiState.Success("Código enviado para seu e-mail!")
                setEtapa(2)
            }

            override fun onFailure(exception: Exception?) {
                val errorMessage = when {
                    exception?.message?.contains("UserNotFoundException", ignoreCase = true) == true ->
                        "E-mail não encontrado. Verifique se o e-mail está correto."

                    exception?.message?.contains("LimitExceededException", ignoreCase = true) == true ->
                        "Muitas tentativas. Tente novamente em alguns minutos."

                    exception?.message?.contains("InvalidParameterException", ignoreCase = true) == true ->
                        "E-mail inválido. Verifique o formato do e-mail."

                    else -> "Erro ao solicitar redefinição de senha. Tente novamente."
                }
                _uiState.value = EsqueciMinhaSenhaUiState.Error(errorMessage)
            }
        })
    }

    // Método para reenviar código
    fun reenviarCodigo() {
        if (email.trim().isEmpty()) {
            _uiState.value = EsqueciMinhaSenhaUiState.Error("Email não encontrado. Reinicie o fluxo.")
            setEtapa(1)
            return
        }

        _uiState.value = EsqueciMinhaSenhaUiState.Loading

        // Limpar sessão anterior e solicitar novo código
        cognitoAuthManager.clearResetSession()

        cognitoAuthManager.forgotPassword(email.trim(), object : ForgotPasswordHandler {
            override fun onSuccess() {
                _showSuccessDialog.value = true
                _uiState.value = EsqueciMinhaSenhaUiState.Idle
            }

            override fun getResetCode(continuation: ForgotPasswordContinuation?) {
                _uiState.value = EsqueciMinhaSenhaUiState.Success("Novo código enviado para seu e-mail!")
                // Limpar código anterior
                codigo = ""
            }

            override fun onFailure(exception: Exception?) {
                val errorMessage = when {
                    exception?.message?.contains("LimitExceededException", ignoreCase = true) == true ->
                        "Muitas tentativas de reenvio. Aguarde alguns minutos."

                    else -> "Erro ao reenviar código. Tente novamente."
                }
                _uiState.value = EsqueciMinhaSenhaUiState.Error(errorMessage)
            }
        })
    }
    // Validação local do código (formato e sessão ativa)
    fun validarCodigoLocal(onCodigoValido: () -> Unit) {
        if (codigo.length != 6) {
            _uiState.value = EsqueciMinhaSenhaUiState.Error("Digite um código de 6 dígitos.")
            return
        }

        // Verificar se temos uma sessão ativa de reset
        if (!cognitoAuthManager.hasActiveResetSession()) {
            _uiState.value = EsqueciMinhaSenhaUiState.Error("Sessão expirada. Solicite um novo código.")
            setEtapa(1)
            return
        }

        // Validação local passou, avançar para definir nova senha
        _uiState.value = EsqueciMinhaSenhaUiState.Idle
        onCodigoValido()
    }

    // Método para confirmar código + nova senha (chamada única ao Cognito)
    fun confirmarNovaSenha(onSuccess: () -> Unit) {
        // Validações locais
        if (codigo.length != 6) {
            _uiState.value = EsqueciMinhaSenhaUiState.Error("Digite um código de 6 dígitos.")
            return
        }

        if (novaSenha.isBlank() || confirmarSenha.isBlank()) {
            _uiState.value = EsqueciMinhaSenhaUiState.Error("Preencha todos os campos.")
            return
        }

        if (novaSenha != confirmarSenha) {
            _uiState.value = EsqueciMinhaSenhaUiState.Error("As senhas não coincidem.")
            return
        }

        if (novaSenha.length < 8) {
            _uiState.value = EsqueciMinhaSenhaUiState.Error("A senha deve ter pelo menos 8 caracteres.")
            return
        }

        // Validação de política de senha
        if (!novaSenha.any { it.isUpperCase() } ||
            !novaSenha.any { it.isLowerCase() } ||
            !novaSenha.any { it.isDigit() }) {
            _uiState.value = EsqueciMinhaSenhaUiState.Error("A senha deve conter pelo menos uma letra maiúscula, uma minúscula e um número.")
            return
        }

        _uiState.value = EsqueciMinhaSenhaUiState.Loading

        // Chamar confirmPassword do Cognito (código + senha juntos)
        cognitoAuthManager.confirmPassword(codigo, novaSenha) { success, error ->
            viewModelScope.launch {
                if (success) {
                    _uiState.value = EsqueciMinhaSenhaUiState.Idle
                    _showSuccessDialog.value = true
                } else {
                    val errorMessage = error ?: "Erro ao alterar senha. Tente novamente."
                    _uiState.value = EsqueciMinhaSenhaUiState.Error(errorMessage)

                    // Se erro de código, voltar para etapa do código
                    if (errorMessage.contains("Código de verificação inválido", ignoreCase = true) ||
                        errorMessage.contains("Código expirado", ignoreCase = true)) {
                        setEtapa(2)
                        codigo = "" // Limpar código inválido
                    }
                }
            }
        }
    }

    fun limparErro() {
        if (_uiState.value is EsqueciMinhaSenhaUiState.Error) {
            _uiState.value = EsqueciMinhaSenhaUiState.Idle
        }
    }

    fun dismissSuccessDialog() {
        _showSuccessDialog.value = false
    }
}