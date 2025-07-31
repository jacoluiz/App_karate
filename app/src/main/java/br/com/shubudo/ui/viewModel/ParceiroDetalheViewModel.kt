package br.com.shubudo.ui.viewModel

import ParceiroDetalheUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.repositories.ParceiroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParceiroDetalheViewModel @Inject constructor(
    private val parceiroRepository: ParceiroRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ParceiroDetalheUiState())
    val uiState: StateFlow<ParceiroDetalheUiState> = _uiState.asStateFlow()

    fun carregarParceiro(parceiroId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val parceiros = parceiroRepository.getParceiros().first()
                val parceiro = parceiros.find { it._id == parceiroId }

                if (parceiro != null) {
                    _uiState.update {
                        it.copy(
                            parceiro = parceiro,
                            isLoading = false,
                            error = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Parceiro não encontrado"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
