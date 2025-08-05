package br.com.shubudo.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager
import br.com.shubudo.repositories.AcademiaRepository
import br.com.shubudo.repositories.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecursosViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val academiaRepository: AcademiaRepository
) : ViewModel() {

    private val _nomesAcademias = MutableStateFlow<Map<String, String>>(emptyMap())
    val nomesAcademias = _nomesAcademias.asStateFlow()

    fun carregarNomesAcademias() {
        viewModelScope.launch {
            try {
                val academias = academiaRepository.getAcademias().first()
                val mapa = academias.associate { it._id to it.nome }
                _nomesAcademias.value = mapa
            } catch (e: Exception) {
                Log.e("RecursosViewModel", "Erro ao carregar nomes das academias", e)
            }
        }
    }

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
