package br.com.shubudo.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager.usuarioLogado
import br.com.shubudo.model.Usuario
import br.com.shubudo.repositories.AcademiaRepository
import br.com.shubudo.repositories.UsuarioRepository
import br.com.shubudo.ui.uistate.EditarPerfilUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditarPerfilViewModel @Inject constructor(
    private val repository: UsuarioRepository,
    private val academiaRepository: AcademiaRepository,
) : ViewModel() {

    // Job para cancelar/reiniciar a coleta de dados se necessário
    private var currentUiStateJob: Job? = null

    // Fluxo que expõe o estado de edição de perfil
    private val _uiState = MutableStateFlow<EditarPerfilUiState>(EditarPerfilUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadUiState()
    }

    /**
     * Inicia ou reinicia a coleta do usuário salvo.
     * Dependendo se encontrar um usuário ou não, atualiza o estado para Empty ou Success.
     */
    private fun loadUiState() {
        currentUiStateJob?.cancel()
        currentUiStateJob = viewModelScope.launch {
            repository.getUsuario()
                .onStart {
                    _uiState.update { EditarPerfilUiState.Loading }
                }
                .collectLatest { usuario ->
                    if (usuario == null) {
                        _uiState.update { EditarPerfilUiState.Empty }
                    } else {
                        loadUsuario(usuario)
                    }
                }
        }
    }

    /**
     * Atualiza o _uiState para Success com os dados do usuário.
     */
    private fun loadUsuario(usuario: Usuario) {
        _uiState.update {
            EditarPerfilUiState.Success(
                id = usuario._id ?: "",
                nome = usuario.nome,
                username = usuario.username,
                email = usuario.email,
                corFaixa = usuario.corFaixa,
                idade = usuario.idade,
                peso = usuario.peso,
                altura = usuario.altura,
                dan = usuario.dan,
                academia = usuario.academia,
                tamanhoFaixa = usuario.tamanhoFaixa,
                lesaoOuLaudosMedicos = usuario.lesaoOuLaudosMedicos,
                registroAKSD = usuario.registroAKSD,
                perfis = usuario.perfis,
                status = usuario.status,
                professorEm = usuario.professorEm,
                academias = (_uiState.value as? EditarPerfilUiState.Success)?.academias
                    ?: emptyList()
            )
        }
        loadAcademias()
    }

    /**
     * Carrega a lista de academias disponíveis
     */
    private fun loadAcademias() {
        viewModelScope.launch {
            try {
                academiaRepository.getAcademias().collectLatest { academias ->
                    val currentState = _uiState.value
                    if (currentState is EditarPerfilUiState.Success) {
                        _uiState.update {
                            currentState.copy(academias = academias)
                        }
                    }
                }
            } catch (e: Exception) {
                // Em caso de erro, mantém a lista vazia
            }
        }
    }

    /**
     * Carrega um usuário específico por ID para edição administrativa
     */
    fun carregarUsuarioPorId(usuarioId: String) {
        currentUiStateJob?.cancel()
        currentUiStateJob = viewModelScope.launch {
            _uiState.update { EditarPerfilUiState.Loading }
            try {
                val usuario = repository.getUsuarioPorId(usuarioId)
                if (usuario != null) {
                    loadUsuario(usuario)
                } else {
                    _uiState.update { EditarPerfilUiState.Empty }
                }
            } catch (e: Exception) {
                _uiState.update { EditarPerfilUiState.Empty }
            }
        }
    }

    /**
     * Chama o repositório para atualizar o usuário no backend (PUT /usuarios/:id).
     * Em caso de sucesso, atualiza o estado com o objeto retornado.
     */
    fun salvarPerfil(
        id: String,
        nome: String,
        username: String,
        email: String,
        corFaixa: String,
        idade: String,
        peso: String,
        altura: String,
        dan: Int,
        academia: String,
        tamanhoFaixa: String,
        lesaoOuLaudosMedicos: String,
        registroAKSD: String,
        perfis: List<String> = listOf("aluno"),
        professorEm: List<String> = listOf(""),
        status: String = "ativo",
        onSave: () -> Unit = {},
        onError: (Throwable) -> Unit = {}, // <-- Adicionado
        context: Context
    ) {
        viewModelScope.launch {
            try {
                val professorEmFinal = if ("professor" in perfis) professorEm else emptyList()

                val usuarioAtualizado = Usuario(
                    _id = id,
                    nome = nome,
                    username = username,
                    email = email,
                    corFaixa = corFaixa,
                    idade = idade,
                    peso = peso,
                    altura = altura,
                    dan = dan,
                    academia = academia,
                    tamanhoFaixa = tamanhoFaixa,
                    lesaoOuLaudosMedicos = lesaoOuLaudosMedicos,
                    registroAKSD = registroAKSD,
                    perfis = perfis,
                    status = status,
                    professorEm = professorEmFinal
                )
                val resultado: Usuario? = if (usuarioLogado?._id == usuarioAtualizado._id) {
                    repository.atualizarUsuario(context, usuarioAtualizado)
                } else {
                    repository.atualizarUsuarioAdm(context, usuarioAtualizado)
                }

                if (resultado != null) {
                    loadUsuario(resultado)
                    onSave()
                } else {
                    onError(Exception("Erro ao salvar o usuário"))
                }
            } catch (e: Exception) {
                onError(e) // <-- Chamado em caso de erro
            }
        }
    }
}

