package br.com.shubudo.ui.viewModel.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.model.Usuario
import br.com.shubudo.repositories.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioListViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadUsuarios()
    }

    fun loadUsuarios() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                usuarioRepository.getUsuarios().collect { usuarios ->
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