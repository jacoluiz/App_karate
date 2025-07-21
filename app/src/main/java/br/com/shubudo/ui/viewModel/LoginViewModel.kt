package br.com.shubudo.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager
import br.com.shubudo.repositories.UsuarioRepository
import br.com.shubudo.ui.uistate.LoginUiState
import com.amazonaws.mobileconnectors.cognitoidentityprovider.exceptions.CognitoIdentityProviderException
import com.amazonaws.services.cognitoidentityprovider.model.NotAuthorizedException
import com.amazonaws.services.cognitoidentityprovider.model.UserNotConfirmedException
import com.amazonaws.services.cognitoidentityprovider.model.UserNotFoundException
import com.amazonaws.services.cognitoidentityprovider.model.InvalidParameterException
import com.amazonaws.services.cognitoidentityprovider.model.CodeMismatchException
import com.amazonaws.services.cognitoidentityprovider.model.ExpiredCodeException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun login(username: String, password: String, themeViewModel: ThemeViewModel) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Usuário e senha são obrigatórios.")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            try {
                val result = repository.login(username.trim(), password)

                if (result == null) {
                    _uiState.value = LoginUiState.Error("Usuário ou senha inválidos.")
                    return@launch
                }

                // Login OK
                SessionManager.usuarioLogado = result
                themeViewModel.changeThemeFaixa(result.corFaixa)
                _uiState.value = LoginUiState.Success(result)

            } catch (e: UserNotConfirmedException) {
                // Usuário existe mas não foi confirmado por email
                // Só redireciona se chegou até aqui (senha estava correta)
                Log.w("LoginViewModel", "Usuário não confirmado: ${e.message}")
                val corFaixa = repository.getCorFaixaLocal(username) ?: "branca"
                _uiState.value = LoginUiState.NavigateToConfirmEmail(username, corFaixa)

            } catch (e: NotAuthorizedException) {
                // Credenciais incorretas (usuário ou senha)
                Log.w("LoginViewModel", "Credenciais incorretas: ${e.message}")
                val message = e.message ?: ""
                when {
                    message.contains("Incorrect username or password", ignoreCase = true) -> {
                        _uiState.value = LoginUiState.Error("Usuário ou senha incorretos.")
                    }
                    message.contains("User does not exist", ignoreCase = true) -> {
                        _uiState.value = LoginUiState.Error("Usuário não encontrado.")
                    }
                    else -> {
                        _uiState.value = LoginUiState.Error("Usuário ou senha incorretos.")
                    }
                }

            } catch (e: UserNotFoundException) {
                // Usuário não existe
                Log.w("LoginViewModel", "Usuário não encontrado: ${e.message}")
                _uiState.value = LoginUiState.Error("Usuário não encontrado.")

            } catch (e: CognitoIdentityProviderException) {
                // Outros erros do Cognito
                val errorType = e::class.simpleName ?: ""
                val message = e.message ?: ""

                Log.w("LoginViewModel", "Erro Cognito: $errorType - $message")

                // Tratamento de mensagens em português
                when {
                    message.contains("Invalid email address format", ignoreCase = true) -> {
                        _uiState.value = LoginUiState.Error("Formato de email inválido.")
                    }
                    message.contains("Password attempts exceeded", ignoreCase = true) -> {
                        _uiState.value = LoginUiState.Error("Muitas tentativas de login. Tente novamente mais tarde.")
                    }
                    message.contains("User is disabled", ignoreCase = true) -> {
                        _uiState.value = LoginUiState.Error("Usuário desabilitado. Entre em contato com o suporte.")
                    }
                    message.contains("User pool does not exist", ignoreCase = true) -> {
                        _uiState.value = LoginUiState.Error("Erro de configuração. Entre em contato com o suporte.")
                    }
                    message.contains("Network error", ignoreCase = true) -> {
                        _uiState.value = LoginUiState.Error("Erro de conexão. Verifique sua internet e tente novamente.")
                    }
                    message.contains("Invalid parameter", ignoreCase = true) -> {
                        _uiState.value = LoginUiState.Error("Dados inválidos. Verifique as informações e tente novamente.")
                    }
                    else -> {
                        Log.e("LoginViewModel", "Erro não tratado: $errorType - $message", e)
                        _uiState.value = LoginUiState.Error("Erro inesperado. Tente novamente mais tarde.")
                    }
                }
            }
            catch (e: Exception) {
                Log.e("LoginViewModel", "Erro inesperado: ${e.message}", e)
                val message = when {
                    e.message?.contains("timeout", ignoreCase = true) == true -> {
                        "Tempo limite excedido. Verifique sua conexão e tente novamente."
                    }
                    e.message?.contains("network", ignoreCase = true) == true -> {
                        "Erro de conexão. Verifique sua internet e tente novamente."
                    }
                    else -> {
                        "Erro inesperado. Tente novamente mais tarde."
                    }
                }
                _uiState.value = LoginUiState.Error(message)
            }
        }
    }

    fun resetUiState() {
        _uiState.value = LoginUiState.Idle
    }
}
