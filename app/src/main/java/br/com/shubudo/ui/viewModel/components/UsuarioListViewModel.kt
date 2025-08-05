package br.com.shubudo.ui.viewModel.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager.idAcademiaVisualizacao
import br.com.shubudo.model.Academia
import br.com.shubudo.model.Usuario
import br.com.shubudo.repositories.AcademiaRepository
import br.com.shubudo.repositories.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                val usuarios = usuarioRepository.getUsuariosPorAcademia(idAcademiaVisualizacao)
                _usuarios.value = usuarios
            } catch (e: Exception) {
                e.printStackTrace()
                _usuarios.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}