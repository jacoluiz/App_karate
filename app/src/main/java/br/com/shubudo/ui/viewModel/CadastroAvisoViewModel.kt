package br.com.shubudo.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager.idAcademiaVisualizacao
import br.com.shubudo.model.Usuario
import br.com.shubudo.repositories.AvisoRepository
import br.com.shubudo.repositories.UsuarioRepository
import br.com.shubudo.ui.uistate.CadastroAvisoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CadastroAvisoViewModel @Inject constructor(
    private val avisoRepository: AvisoRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CadastroAvisoUiState>(CadastroAvisoUiState.Loading)
    val uiState: StateFlow<CadastroAvisoUiState> = _uiState.asStateFlow()

    private var todosUsuarios: List<Usuario> = emptyList()

    fun loadUsuarios() {
        viewModelScope.launch {
            _uiState.value = CadastroAvisoUiState.Loading
            try {
                val usuarios = usuarioRepository.getUsuariosPorAcademia(idAcademiaVisualizacao)
                todosUsuarios = usuarios.filter { it.status == "ativo" }
                _uiState.value = CadastroAvisoUiState.Success(
                    usuarios = todosUsuarios,
                    usuariosFiltrados = emptyList()
                )
            } catch (e: Exception) {
                _uiState.value =
                    CadastroAvisoUiState.Error("Erro ao carregar usuários: ${e.message}")
            }
        }
    }


    fun selecionarUsuariosPorFaixa(corFaixa: String): Set<Usuario> {
        return todosUsuarios.filter {
            it.status == "ativo" && it.corFaixa.equals(corFaixa, ignoreCase = true)
        }.toSet()
    }

    fun searchUsuarios(query: String) {
        val currentState = _uiState.value
        if (currentState is CadastroAvisoUiState.Success) {
            val filtrados = if (query.isBlank()) {
                emptyList()
            } else {
                todosUsuarios.filter { usuario ->
                    usuario.nome.contains(query, ignoreCase = true) ||
                            usuario.email.contains(query, ignoreCase = true) ||
                            usuario.username.contains(query, ignoreCase = true)
                }
            }
            _uiState.value = currentState.copy(usuariosFiltrados = filtrados)
        }
    }

    fun criarOuAtualizarAviso(
        titulo: String,
        conteudo: String,
        publicoAlvo: List<String>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = CadastroAvisoUiState.Loading

                val destinatarios = publicoAlvo.ifEmpty {
                    todosUsuarios
                        .filter { it.academiaId == idAcademiaVisualizacao }
                        .map { it.email }
                }

                val currentState = _uiState.value
                if (currentState is CadastroAvisoUiState.Success && currentState.isEditando) {
                    avisoRepository.editarAviso(
                        id = currentState.avisoId ?: "",
                        titulo = titulo,
                        conteudo = conteudo,
                        publicoAlvo = destinatarios,
                        academia = idAcademiaVisualizacao
                    )
                } else {
                    avisoRepository.criarAviso(
                        titulo = titulo,
                        conteudo = conteudo,
                        publicoAlvo = destinatarios,
                        academia = idAcademiaVisualizacao
                    )
                }


                onSuccess()
            } catch (e: Exception) {
                _uiState.value = CadastroAvisoUiState.Error("Erro ao salvar aviso: ${e.message}")
                onError(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun carregarAviso(avisoId: String) {
        viewModelScope.launch {
            try {
                if (todosUsuarios.isEmpty()) {
                    usuarioRepository.getUsuarios().collect { usuarios ->
                        todosUsuarios = usuarios.filter { it.status == "ativo" }
                    }
                }

                val aviso = avisoRepository.getAvisoById(avisoId)
                Log.d("CadastroAvisoViewModel", "Carregado aviso: $aviso")
                aviso?.let {
                    val publicoSelecionado = it.publicoAlvo.mapNotNull { email ->
                        todosUsuarios.find { user -> user.email == email }
                    }
                    Log.d("CadastroAvisoViewModel", "Público selecionado: $publicoSelecionado")

                    _uiState.value = CadastroAvisoUiState.Success(
                        titulo = it.titulo,
                        conteudo = it.conteudo,
                        publicoAlvo = it.publicoAlvo,
                        isEditando = true,
                        avisoId = it.id,
                        usuarios = todosUsuarios,
                        usuariosFiltrados = emptyList(),
                        usuariosSelecionados = publicoSelecionado.toSet()
                    )
                }
            } catch (e: Exception) {
                _uiState.value = CadastroAvisoUiState.Error("Erro ao carregar aviso: ${e.message}")
            }
        }
    }
}
