package br.com.shubudo.ui.viewModel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.repositories.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EsqueciMinhaSenhaViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    val email = mutableStateOf("")
    val codigo = mutableStateOf("")
    val novaSenha = mutableStateOf("")
    val etapa = mutableIntStateOf(1)

    val mensagemErro = mutableStateOf<String?>(null)
    val sucesso = mutableStateOf(false)

    fun solicitarCodigo() {
        val emailTrimmed = email.value.trim()
        if (emailTrimmed.isEmpty()) {
            mensagemErro.value = "Digite um e-mail válido."
            return
        }

        viewModelScope.launch {
            val resultado = usuarioRepository.iniciarEsqueciSenha(emailTrimmed)
            if (resultado) {
                etapa.intValue = 2
            } else {
                mensagemErro.value = "Erro ao solicitar redefinição. Verifique o email."
            }
        }
    }


    fun redefinirSenha() {
        viewModelScope.launch {
            val resultado = usuarioRepository.confirmarNovaSenha(
                codigo.value,
                novaSenha.value
            )
            if (resultado) {
                mensagemErro.value = null // <<< LIMPA o erro anterior
                sucesso.value = true
            } else {
                mensagemErro.value = "Erro ao redefinir a senha."
            }
        }
    }
}
