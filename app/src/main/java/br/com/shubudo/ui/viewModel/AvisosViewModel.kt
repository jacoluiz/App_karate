package br.com.shubudo.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager
import br.com.shubudo.repositories.AvisoRepository
import br.com.shubudo.ui.uistate.AvisosUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvisosViewModel @Inject constructor(
    private val avisoRepository: AvisoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AvisosUiState>(AvisosUiState.Loading)
    val uiState: StateFlow<AvisosUiState> = _uiState.asStateFlow()
    private val _avisoParaEditar = MutableSharedFlow<String>()
    val avisoParaEditar = _avisoParaEditar.asSharedFlow()

    init {
        loadAvisos()
    }

    private fun loadAvisos() {
        viewModelScope.launch {
            _uiState.value = AvisosUiState.Loading

            try {
                // Tenta atualizar da API (ignora falha)
                avisoRepository.refreshAvisos()
            } catch (_: Exception) {
            }

            avisoRepository.getAvisos()
                .catch {
                    _uiState.value = AvisosUiState.Empty
                }
                .collect { avisos ->
                    val usuario = SessionManager.usuarioLogado
                    val emailUsuario = usuario?.email.orEmpty()
                    val perfilUsuario = usuario?.perfil.orEmpty()

                    val avisosFiltrados = if (perfilUsuario == "adm") {
                        avisos // Admin vê todos
                    } else {
                        avisos.filter { aviso ->
                            aviso.publicoAlvo.isEmpty() || aviso.publicoAlvo.contains(emailUsuario)
                        }
                    }

                    _uiState.value = if (avisosFiltrados.isEmpty()) {
                        AvisosUiState.Empty
                    } else {
                        AvisosUiState.Success(avisosFiltrados)
                    }
                }
        }
    }

    fun deletarAviso(id: String) {
        viewModelScope.launch {
            try {
                avisoRepository.deletarAviso(id)

                // Atualiza lista manualmente após a exclusão
                val listaAtual = (_uiState.value as? AvisosUiState.Success)?.avisos ?: emptyList()
                val novaLista = listaAtual.filterNot { it.id == id }

                _uiState.value = if (novaLista.isEmpty()) {
                    AvisosUiState.Empty
                } else {
                    AvisosUiState.Success(novaLista)
                }

            } catch (e: Exception) {
                Log.e("AvisosViewModel", "Erro ao deletar aviso", e)
            }
        }
    }

    fun editarAviso(id: String) {
        viewModelScope.launch {
            _avisoParaEditar.emit(id)
        }
    }
}
