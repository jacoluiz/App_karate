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
        // Validação básica
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Usuário e senha são obrigatórios.")
            return
        }

        // Validação básica
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Usuário e senha são obrigatórios.")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                Log.i("LoginViewModel", "Iniciando login para: $username")

                val result = repository.login(username.trim(), password)
                if (result != null) {
                    Log.i("LoginViewModel", "Login bem-sucedido para: ${result.username}")
                    Log.i("LoginViewModel", "Login bem-sucedido para: ${result.username}")
                    _uiState.value = LoginUiState.Success(result)
                    SessionManager.usuarioLogado = result
                    themeViewModel.changeThemeFaixa(result.corFaixa)
                } else {
                    Log.w("LoginViewModel", "Login falhou - resultado nulo")
                    Log.w("LoginViewModel", "Login falhou - resultado nulo")
                    _uiState.value = LoginUiState.Error("Usuário ou senha inválidos.")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Erro ao fazer login: ${e.message}", e)

                // Tratar diferentes tipos de erro
                val errorMessage = when {
                    e.message?.contains("User does not exist", ignoreCase = true) == true ->
                        "Usuário não encontrado. Verifique suas credenciais."

                    e.message?.contains(
                        "Incorrect username or password",
                        ignoreCase = true
                    ) == true ->
                        "Usuário ou senha incorretos."

                    e.message?.contains("User is not confirmed", ignoreCase = true) == true ->
                        "Usuário não confirmado. Verifique seu email."

                    e.message?.contains("Network", ignoreCase = true) == true ->
                        "Erro de conexão. Verifique sua internet."

                    else -> "Erro ao tentar fazer login. Tente novamente."
                }

                _uiState.value = LoginUiState.Error(errorMessage)
            }
        }
    }

    fun resetUiState() {
        _uiState.value = LoginUiState.Idle
    }
}