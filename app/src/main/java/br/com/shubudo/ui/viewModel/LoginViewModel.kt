package br.com.shubudo.ui.viewModel

import android.content.Context
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
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val result = repository.login(username, password)
                result?.let {
                    _uiState.value = LoginUiState.Success(it)
                    SessionManager.usuarioLogado = it
                    themeViewModel.changeThemeFaixa(it.corFaixa)
                } ?: run {
                    _uiState.value = LoginUiState.Error("Usuário ou senha inválidos.")
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("${e.message}")
            }
        }
    }


    fun resetUiState() {
        _uiState.value = LoginUiState.Idle // Redefine para o estado inicial
    }
}
