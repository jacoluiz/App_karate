package br.com.shubudo.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager
import br.com.shubudo.model.Usuario
import br.com.shubudo.repositories.UsuarioRepository
import br.com.shubudo.ui.uistate.PerfilUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val repository: UsuarioRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private var currentUiStateJob: Job? = null
    private val _uiState = MutableStateFlow<PerfilUiState>(
        PerfilUiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadUiState()
    }

    private fun loadUiState() {
        currentUiStateJob?.cancel()
        currentUiStateJob = viewModelScope.launch {
            repository.getUsuario().onStart {
                _uiState.update { PerfilUiState.Loading }
            }.collectLatest { usuario ->
                if (usuario == null) {
                    SessionManager.limparSessao(context) // limpa sessão se usuário não encontrado
                    _uiState.update { PerfilUiState.Empty }
                } else {
                    SessionManager.salvarUsuario(context, usuario)// manter atualizado
                    loadUsuario(usuario)
                }
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            repository.logout()
            SessionManager.limparSessao(context) // limpa sessão se usuário não encontrado
            _uiState.value = PerfilUiState.Empty
            onLoggedOut()
        }
    }

    private fun loadUsuario(usuario: Usuario) {
        _uiState.update {
            PerfilUiState.Success(
                nome = usuario.nome,
                username = usuario.username,
                email = usuario.email,
                corFaixa = usuario.corFaixa,
                idade = usuario.idade,
                peso = usuario.peso,
                altura = usuario.altura,
                dan = usuario.dan,
                academia = usuario.academia,
                tamanhoFaixa = usuario.tamanhoFaixa,
                registroAKSD = usuario.registroAKSD
            )
        }
    }
}

