package br.com.shubudo.ui.viewModel

import AcademiaUiState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.repositories.AcademiaRepository
import br.com.shubudo.repositories.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AcademiaViewModel @Inject constructor(
    private val academiaRepository: AcademiaRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AcademiaUiState>(AcademiaUiState.Loading)
    val uiState: StateFlow<AcademiaUiState> = _uiState.asStateFlow()

    init {
        carregarAcademias()
    }

    fun carregarAcademias() {
        viewModelScope.launch {
            _uiState.value = AcademiaUiState.Loading

            try {
                academiaRepository.refreshAcademias()
            } catch (_: Exception) {
            }

            academiaRepository.getAcademias()
                .catch {
                    _uiState.value = AcademiaUiState.Empty
                }
                .collect { academias ->
                    _uiState.value = if (academias.isEmpty()) {
                        AcademiaUiState.Empty
                    } else {
                        AcademiaUiState.Success(academias)
                    }
                }
        }
    }

    fun deletarAcademiaComVerificacao(
        academiaId: String,
        onFalha: () -> Unit,
        onSucesso: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val usuarios = usuarioRepository.getUsuariosPorAcademia(academiaId)
                if (usuarios.isNotEmpty()) {
                    onFalha() // Existem usuários, não pode deletar
                } else {
                    deletarAcademia(academiaId)
                    onSucesso()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onFalha() // Em caso de erro, também mostra o erro
            }
        }
    }

    fun deletarAcademia(id: String) {
        viewModelScope.launch {
            try {
                academiaRepository.deletarAcademia(id)

                // Atualiza lista manualmente após exclusão
                val listaAtual =
                    (_uiState.value as? AcademiaUiState.Success)?.academias ?: emptyList()
                val novaLista = listaAtual.filterNot { it._id == id }

                _uiState.value = if (novaLista.isEmpty()) {
                    AcademiaUiState.Empty
                } else {
                    AcademiaUiState.Success(novaLista)
                }

            } catch (e: Exception) {
                Log.e("AcademiaViewModel", "Erro ao deletar academia", e)
            }
        }
    }
}
