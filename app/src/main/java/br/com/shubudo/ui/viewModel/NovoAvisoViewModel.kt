package br.com.shubudo.ui.viewModel

import androidx.lifecycle.ViewModel
import br.com.shubudo.model.Aviso
import br.com.shubudo.repositories.AvisoRepository
import br.com.shubudo.ui.uistate.NovoAvisoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NovoAvisoViewModel @Inject constructor(
    private val repository: AvisoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NovoAvisoUiState>(NovoAvisoUiState.Idle)
    val uiState = _uiState.asStateFlow()

    suspend fun salvarAviso(aviso: Aviso): Aviso? {
        _uiState.value = NovoAvisoUiState.Loading
        return try {
            val novoAviso = repository.criarAviso(aviso)
            _uiState.value = if (novoAviso != null) NovoAvisoUiState.Success(novoAviso)
            else NovoAvisoUiState.Error("Erro ao criar aviso.")
            novoAviso
        } catch (e: Exception) {
            _uiState.value = NovoAvisoUiState.Error(e.message ?: "Erro desconhecido")
            null
        }
    }

    suspend fun atualizarAviso(aviso: Aviso): Aviso? {
        _uiState.value = NovoAvisoUiState.Loading
        return try {
            val avisoAtualizado = repository.atualizarAviso(aviso)
            _uiState.value = if (avisoAtualizado != null) NovoAvisoUiState.Success(avisoAtualizado)
            else NovoAvisoUiState.Error("Erro ao atualizar aviso.")
            avisoAtualizado
        } catch (e: Exception) {
            _uiState.value = NovoAvisoUiState.Error(e.message ?: "Erro desconhecido")
            null
        }
    }

    suspend fun buscarAviso(avisoId: String): Aviso? {
        return repository.getAvisoById(avisoId)
    }
}
