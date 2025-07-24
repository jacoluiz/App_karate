package br.com.shubudo.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager
import br.com.shubudo.repositories.UsuarioRepository
import br.com.shubudo.ui.uistate.LoginUiState
import com.amazonaws.services.cognitoidentityprovider.model.NotAuthorizedException
import com.amazonaws.services.cognitoidentityprovider.model.UserNotConfirmedException
import com.amazonaws.services.cognitoidentityprovider.model.UserNotFoundException
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

    fun login(context : Context,username: String, password: String, themeViewModel: ThemeViewModel) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Usuário e senha são obrigatórios.")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            try {
                val result = repository.login(context, username.trim(), password)

                if (result == null) {
                    _uiState.value = LoginUiState.Error("Usuário ou senha inválidos.")
                    return@launch
                }

                SessionManager.usuarioLogado = result
                themeViewModel.changeThemeFaixa(result.corFaixa)
                _uiState.value = LoginUiState.Success(result)

            } catch (e: NotAuthorizedException) {
                val message = e.message ?: ""

                Log.w("LoginViewModel", "Erro Cognito - NotAuthorizedException: $message")

                when {
                    message.contains("Incorrect username or password", ignoreCase = true) -> {
                        _uiState.value = LoginUiState.Error("Usuário ou senha incorretos.")
                    }

                    message.contains("User is not confirmed", ignoreCase = true) -> {
                        val corFaixa = repository.getCorFaixaLocal(username)
                            ?: themeViewModel.getCurrentFaixa()
                            ?: "branca"
                        _uiState.value = LoginUiState.NavigateToConfirmEmail(username, corFaixa)
                        return@launch
                    }

                    else -> {
                        _uiState.value =
                            LoginUiState.Error("Não foi possível fazer login. Verifique os dados.")
                    }
                }

            } catch (e: UserNotFoundException) {
                _uiState.value = LoginUiState.Error("Usuário não encontrado.")

            } catch (e: UserNotConfirmedException) {
                Log.w("LoginViewModel", "Usuário não confirmado: ${e.message}")
                val corFaixa = repository.getCorFaixaLocal(username)
                    ?: themeViewModel.getCurrentFaixa()
                    ?: "branca"
                _uiState.value = LoginUiState.NavigateToConfirmEmail(username, corFaixa)
                return@launch

            } catch (e: Exception) {
                val message = e.message ?: "Erro desconhecido"
                Log.e("LoginViewModel", "Erro inesperado: $message", e)

                if (_uiState.value !is LoginUiState.NavigateToConfirmEmail) {
                    _uiState.value = LoginUiState.Error("Erro inesperado: $message")
                }
            }
        }
    }

    fun resetUiState() {
        _uiState.value = LoginUiState.Idle
    }
}
