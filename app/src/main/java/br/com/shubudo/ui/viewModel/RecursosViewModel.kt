package br.com.shubudo.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager
import br.com.shubudo.repositories.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecursosViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    fun atualizarUsuarioLogado(context: Context) {
        val usuarioAtual = SessionManager.usuarioLogado
        if (usuarioAtual != null) {
            viewModelScope.launch {
                val atualizado =
                    usuarioRepository.getUsuarioPorId(usuarioAtual._id ?: return@launch)
                atualizado?.let {
                    SessionManager.salvarUsuario(context, it)
                }
            }
        }
    }
}
