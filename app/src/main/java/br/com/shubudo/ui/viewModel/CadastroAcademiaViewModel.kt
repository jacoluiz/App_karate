package br.com.shubudo.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.model.Filial
import br.com.shubudo.repositories.AcademiaRepository
import br.com.shubudo.ui.uistate.CadastroAcademiaUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CadastroAcademiaViewModel @Inject constructor(
    private val academiaRepository: AcademiaRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<CadastroAcademiaUiState>(CadastroAcademiaUiState.Loading)
    val uiState: StateFlow<CadastroAcademiaUiState> = _uiState.asStateFlow()

    fun carregarAcademia(id: String) {
        viewModelScope.launch {
            try {
                val academia = academiaRepository.getAcademiaById(id)
                if (academia != null) {
                    _uiState.value = CadastroAcademiaUiState.Success(
                        nome = academia.nome,
                        descricao = academia.descricao,
                        id = academia._id,
                        isEditando = true
                    )
                } else {
                    _uiState.value = CadastroAcademiaUiState.Error("Academia não encontrada.")
                }
            } catch (e: Exception) {
                _uiState.value =
                    CadastroAcademiaUiState.Error("Erro ao carregar academia: ${e.message}")
            }
        }
    }

    fun criarOuAtualizarAcademia(
        nome: String,
        descricao: String?,
        id: String? = null,
        filiais: List<Filial> ,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = CadastroAcademiaUiState.Loading
                if (id != "" ) {
                    academiaRepository.editarAcademia(
                        id = id ?: "",
                        nome = nome,
                        descricao = descricao.orEmpty(),
                        filiais = filiais
                    )
                } else {
                    academiaRepository.criarAcademia(
                        nome = nome,
                        descricao = descricao.orEmpty(),
                        filiais = filiais
                    )
                }
                onSuccess()
            } catch (e: Exception) {
                _uiState.value =
                    CadastroAcademiaUiState.Error("Erro ao salvar academia: ${e.message}")
                onError(e.message ?: "Erro desconhecido")
            }
        }
    }
}
