package br.com.shubudo.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager
import br.com.shubudo.repositories.UsuarioRepository
import br.com.shubudo.ui.uistate.LoginUiState
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
                Log.i("LoginViewModel", "Iniciando login para: $username")

                val result = try {
                    repository.login(username.trim(), password)
                } catch (e: Exception) {
                    val message = e.message ?: ""

                    when {
                        message.contains("User is not confirmed", ignoreCase = true) -> {
                            val corFaixa = repository.getCorFaixaLocal(username) ?: "branca"
                            _uiState.value = LoginUiState.NavigateToConfirmEmail(username, corFaixa)
                        }

                        message.contains("Incorrect username or password", ignoreCase = true) -> {
                            _uiState.value = LoginUiState.Error("Usuário ou senha incorretos.")
                        }

                        message.contains("User does not exist", ignoreCase = true) -> {
                            _uiState.value = LoginUiState.Error("Usuário não encontrado.")
                        }

                        message.contains("Network", ignoreCase = true) -> {
                            _uiState.value = LoginUiState.Error("Erro de conexão. Verifique sua internet.")
                        }

                        else -> {
                            Log.e("LoginViewModel", "Erro inesperado: ${e.message}", e)
                            _uiState.value = LoginUiState.Error("Erro inesperado: ${e.message}")
                        }
                    }

                    // Para encerrar o bloco
                    return@launch
                }

                if (result == null) {
                    _uiState.value = LoginUiState.Error("Usuário ou senha inválidos.")
                    return@launch
                }

                Log.i("LoginViewModel", "Login bem-sucedido para: ${result.username}")
                _uiState.value = LoginUiState.Success(result)
                SessionManager.usuarioLogado = result
                themeViewModel.changeThemeFaixa(result.corFaixa)

            } catch (e: Exception) {
                Log.e("LoginViewModel", "Erro inesperado fora do try interno: ${e.message}", e)
                _uiState.value = LoginUiState.Error("Erro inesperado: ${e.message}")
            }
        }
    }


    fun resetUiState() {
        _uiState.value = LoginUiState.Idle
    }
}
