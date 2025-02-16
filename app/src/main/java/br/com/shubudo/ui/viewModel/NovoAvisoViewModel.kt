package br.com.shubudo.ui.viewModel

import androidx.lifecycle.ViewModel
import br.com.shubudo.model.Aviso
import br.com.shubudo.repositories.AvisoRepository
import br.com.shubudo.ui.uistate.NovoAvisoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NovoAvisoViewModel @Inject constructor(
    private val repository: AvisoRepository
) : ViewModel() {

    private var currentUiStateJob: Job? = null
    private val _uiState = MutableStateFlow<NovoAvisoUiState>(NovoAvisoUiState.Idle)
    val uiState = _uiState.asStateFlow()

    /**
     * Salva um novo aviso chamando o repositório.
     * Atualiza o estado para Loading, Success ou Error conforme o resultado.
     */
    suspend fun salvarAviso(aviso: Aviso): Aviso? {
        _uiState.value = NovoAvisoUiState.Loading
        return try {
            val novoAviso = repository.criarAviso(aviso)
            if (novoAviso != null) {
                _uiState.value = NovoAvisoUiState.Success(novoAviso)

            } else {
                _uiState.value = NovoAvisoUiState.Error("Erro ao criar aviso.")
            }
            novoAviso
        } catch (e: Exception) {
            _uiState.value = NovoAvisoUiState.Error(e.message ?: "Erro desconhecido")
            null
        }
    }
}
