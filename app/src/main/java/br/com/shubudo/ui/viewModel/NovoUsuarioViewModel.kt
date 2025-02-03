package br.com.shubudo.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.model.Usuario
import br.com.shubudo.repositories.UsuarioRepository
import br.com.shubudo.ui.uistate.CadastroUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class NovoUsuarioViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CadastroUiState>(CadastroUiState.Idle)
    val uiState: StateFlow<CadastroUiState> = _uiState

    fun cadastrarUsuario() {
        viewModelScope.launch {
            _uiState.value = CadastroUiState.Loading
            try {
                val usuario = Usuario(
                    nome = nome,
                    email = email,
                    senha = senha,
                    corFaixa = faixa,
                    peso = peso,
                    altura = altura,
                    username = nome,
                    idade = "0",
                    perfil = "básico",
                )

                usuarioRepository.cadastrarUsuario(usuario)
                _uiState.value = CadastroUiState.Success("Usuário cadastrado com sucesso!")
            } catch (e: Exception) {
                _uiState.value = CadastroUiState.Error("Erro ao cadastrar usuário: ${e.message}")
            }
        }
    }

    var nome by mutableStateOf("")
    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    var confirmarSenha by mutableStateOf("")
    var faixa by mutableStateOf("Branca")
    var peso by mutableStateOf("")
    var altura by mutableStateOf("0,00")
    var senhaAtendeAosRequisitos by mutableStateOf(false)
}
