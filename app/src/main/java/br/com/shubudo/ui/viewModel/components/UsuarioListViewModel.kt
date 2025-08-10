package br.com.shubudo.ui.viewModel.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager.idAcademiaVisualizacao
import br.com.shubudo.SessionManager.perfilAtivo
import br.com.shubudo.model.Academia
import br.com.shubudo.model.Usuario
import br.com.shubudo.repositories.AcademiaRepository
import br.com.shubudo.repositories.UsuarioRepository
import br.com.shubudo.ui.viewModel.EventoDetalheViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioListViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val academiaRepository: AcademiaRepository,
) : ViewModel() {

    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _academias = MutableStateFlow<List<Academia>>(emptyList())
    val academias = _academias.asStateFlow()

    init {
        loadUsuarios()
        loadAcademias()
    }

    fun selecionarUsuariosPorFaixa(corFaixa: String): Set<Usuario> {
        return usuarios.value
            .filter { usuario ->
                usuario.status.equals("ativo", ignoreCase = true) &&
                        usuario.corFaixa.equals(corFaixa, ignoreCase = true)
            }
            .toSet()
    }

    fun searchUsuarios(query: String): List<Usuario> {
        val lista = usuarios as? List<Usuario> ?: emptyList()
        return if (query.isBlank()) {
            emptyList()
        } else {
            lista.filter { usuario ->
                usuario.nome.contains(query, ignoreCase = true) ||
                        usuario.email.contains(query, ignoreCase = true) ||
                        usuario.username.contains(query, ignoreCase = true)
            }
        }
    }

    private fun loadAcademias() {
        viewModelScope.launch {
            val academias = academiaRepository.getAcademias().firstOrNull().orEmpty()
            _academias.value = academias
        }
    }

    fun loadUsuarios() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (perfilAtivo == "adm") {
                    usuarioRepository.getUsuarios().collect { lista ->
                        _usuarios.value = lista
                    }
                } else {
                    val usuarios = usuarioRepository.getUsuariosPorAcademia(idAcademiaVisualizacao)
                    _usuarios.value = usuarios
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _usuarios.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}