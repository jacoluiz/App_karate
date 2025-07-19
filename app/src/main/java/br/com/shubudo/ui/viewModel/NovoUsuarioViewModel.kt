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

    var nome by mutableStateOf("")
    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    var confirmarSenha by mutableStateOf("")
    var faixa by mutableStateOf("Branca")
    var peso by mutableStateOf("")
    var altura by mutableStateOf("0,00")
    var senhaAtendeAosRequisitos by mutableStateOf(false)

    fun cadastrarUsuario() {
        viewModelScope.launch {
            _uiState.value = CadastroUiState.Loading

            if (nome.isBlank() || email.isBlank() || senha.isBlank() || confirmarSenha.isBlank() || peso.isBlank()) {
                _uiState.value = CadastroUiState.Error("Preencha todos os campos obrigatórios.")
                return@launch
            }

            if (senha != confirmarSenha) {
                _uiState.value = CadastroUiState.Error("As senhas não coincidem.")
                return@launch
            }

            try {
                val usuario = Usuario(
                    nome = nome,
                    email = email,
                    senha = senha,
                    corFaixa = faixa,
                    peso = peso,
                    altura = altura,
                    username = email, // Você pode mudar para outro campo como userInput
                    idade = "0",
                    perfil = "básico"
                )

                val resultado = usuarioRepository.cadastrarUsuario(usuario)

                if (resultado != null) {
                    _uiState.value = CadastroUiState.Success("Usuário cadastrado com sucesso!")
                } else {
                    _uiState.value = CadastroUiState.Error("Falha ao cadastrar usuário.")
                }

            } catch (e: Exception) {
                _uiState.value = CadastroUiState.Error("Erro ao cadastrar usuário: ${e.message}")
            }
        }
    }
}
