package br.com.shubudo.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager
import br.com.shubudo.auth.CognitoAuthManager
import br.com.shubudo.repositories.UsuarioRepository
import br.com.shubudo.ui.uistate.ConfirmacaoEmailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmacaoEmailViewModel @Inject constructor(
    private val cognito: CognitoAuthManager,
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConfirmacaoEmailUiState>(ConfirmacaoEmailUiState.Idle)
    val uiState: StateFlow<ConfirmacaoEmailUiState> = _uiState

    var codigo = ""

    var email: String? = null
    private var senha: String? = null

    fun iniciar(email: String, senha: String) {
        this.email = email
        this.senha = senha
    }

    fun confirmarCodigo() {
        val email = this.email
        val senha = this.senha

        if (email.isNullOrBlank() || senha.isNullOrBlank()) {
            _uiState.value = ConfirmacaoEmailUiState.Error("Erro interno: credenciais ausentes. Tente novamente.")
            return
        }

        _uiState.value = ConfirmacaoEmailUiState.Loading

        cognito.confirmCode(email, codigo) { sucesso, erro ->
            if (sucesso) {
                viewModelScope.launch {
                    try {
                        val usuario = usuarioRepository.login(email, senha)
                        SessionManager.usuarioLogado = usuario
                        _uiState.value = ConfirmacaoEmailUiState.Success("Conta confirmada e usuário logado com sucesso!")
                    } catch (e: Exception) {
                        _uiState.value = ConfirmacaoEmailUiState.Error("Erro ao fazer login após confirmação: ${e.message}")
                    }
                }
            } else {
                val mensagemAmigavel = when {
                    erro?.contains("CodeMismatchException", ignoreCase = true) == true ->
                        "Código inválido. Verifique e tente novamente."

                    erro?.contains("ExpiredCodeException", ignoreCase = true) == true ->
                        "O código expirou. Reenvie o código e tente novamente."

                    else -> "Erro ao confirmar o código. Tente novamente."
                }
                _uiState.value = ConfirmacaoEmailUiState.Error(mensagemAmigavel)
            }
        }
    }

    fun reenviarCodigo() {
        val email = this.email

        if (email.isNullOrBlank()) {
            _uiState.value = ConfirmacaoEmailUiState.Error("Email não disponível para reenviar código.")
            return
        }

        _uiState.value = ConfirmacaoEmailUiState.Loading

        cognito.reenviarCodigoConfirmacao(email) { sucesso, erro ->
            if (sucesso) {
                _uiState.value = ConfirmacaoEmailUiState.Idle
            } else {
                _uiState.value =
                    ConfirmacaoEmailUiState.Error(erro ?: "Erro ao reenviar o código.")
            }
        }
    }
}
