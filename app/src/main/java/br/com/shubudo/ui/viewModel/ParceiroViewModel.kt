package br.com.shubudo.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.repositories.ParceiroRepository
import br.com.shubudo.ui.uistate.ParceirosUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParceiroViewModel @Inject constructor(
    private val parceiroRepository: ParceiroRepository
) : ViewModel() {

    private val _parceirosUiState = MutableStateFlow<ParceirosUiState>(ParceirosUiState.Loading)
    val parceirosUiState: StateFlow<ParceirosUiState> = _parceirosUiState.asStateFlow()

    private var fetchJob: Job? = null

    init {
        recarregarParceiros()
    }

    fun recarregarParceiros() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            parceiroRepository.refreshParceiros()
            parceiroRepository.getParceiros().collectLatest { parceiros ->
                if (parceiros.isEmpty()) {
                    _parceirosUiState.update { ParceirosUiState.Empty }
                } else {
                    _parceirosUiState.update { ParceirosUiState.Success(parceiros) }
                }
            }
        }
    }

    fun deletarParceiro(parceiroId: String) {
        viewModelScope.launch {
            try {
                parceiroRepository.deletarParceiro(parceiroId)
                val estadoAtual = _parceirosUiState.value
                if (estadoAtual is ParceirosUiState.Success) {
                    val atualizados = estadoAtual.parceiros.filterNot { it._id == parceiroId }
                    if (atualizados.isEmpty()) {
                        _parceirosUiState.update { ParceirosUiState.Empty }
                    } else {
                        _parceirosUiState.update { ParceirosUiState.Success(atualizados) }
                    }
                }
            } catch (e: Exception) {
                // Erro silencioso por enquanto
            }
        }
    }
}
