package br.com.shubudo.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.auth.CognitoAuthManager
import br.com.shubudo.repositories.UsuarioRepository
import br.com.shubudo.ui.uistate.EsqueciMinhaSenhaUiState
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EsqueciMinhaSenhaViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
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

    fun setEtapa(novaEtapa: Int) {
        _etapa.intValue = novaEtapa
    }

    fun solicitarCodigo() {
        if (email.trim().isEmpty()) {
            _uiState.value = EsqueciMinhaSenhaUiState.Error("Digite um e-mail válido.")
            return
        }

        _uiState.value = EsqueciMinhaSenhaUiState.Loading

        cognitoAuthManager.forgotPassword(email.trim(), object : ForgotPasswordHandler {
            override fun onSuccess() {
                // Caso raro: redefinição sem código
                _uiState.value = EsqueciMinhaSenhaUiState.Success("Senha redefinida com sucesso!")
            }

            override fun getResetCode(continuation: ForgotPasswordContinuation?) {
                // Código enviado com sucesso
                _uiState.value = EsqueciMinhaSenhaUiState.Idle
                setEtapa(2)
            }

            override fun onFailure(exception: Exception?) {
                _uiState.value = EsqueciMinhaSenhaUiState.Error(
                    exception?.localizedMessage ?: "Erro ao solicitar redefinição de senha."
                )
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
                    _uiState.value = EsqueciMinhaSenhaUiState.Success("Senha alterada com sucesso!")
                    // Delay para mostrar mensagem antes de navegar
                    kotlinx.coroutines.delay(1500)
                    onSuccess()
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
        solicitarCodigo()
    }

    // Método para voltar à etapa anterior
    fun voltarEtapa() {
        when (etapa.value) {
            2 -> setEtapa(1)
            3 -> setEtapa(2)
        }
        limparErro()
    }

    // Método para reiniciar o fluxo completamente
    fun reiniciarFluxo() {
        cognitoAuthManager.clearResetSession()
        codigo = ""
        novaSenha = ""
        confirmarSenha = ""
        setEtapa(1)
        _uiState.value = EsqueciMinhaSenhaUiState.Idle
    }

    fun limparErro() {
        if (_uiState.value is EsqueciMinhaSenhaUiState.Error) {
            _uiState.value = EsqueciMinhaSenhaUiState.Idle
        }
    }
}