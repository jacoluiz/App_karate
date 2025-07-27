package br.com.shubudo.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.repositories.GaleriaFotoRepository
import br.com.shubudo.ui.uistate.GaleriaFotosUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GaleriaFotosViewModel @Inject constructor(
    private val galeriaRepository: GaleriaFotoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GaleriaFotosUiState>(GaleriaFotosUiState.Loading)
    val uiState: StateFlow<GaleriaFotosUiState> = _uiState.asStateFlow()

    fun carregarFotos(eventoId: String) {
        Log.d("GaleriaFotosViewModel", "Carregando fotos para o evento: $eventoId")
        viewModelScope.launch {
            _uiState.update { GaleriaFotosUiState.Loading }

            try {
                val fotos = galeriaRepository.getFotosPorEvento(eventoId).first()
                Log.d("GaleriaFotosViewModel", "Fotos carregadas: ${fotos.size}")
                if (fotos.isEmpty()) {
                    _uiState.update { GaleriaFotosUiState.Empty }
                } else {
                    _uiState.update { GaleriaFotosUiState.Success(fotos) }
                }

            } catch (e: Exception) {
                _uiState.update {
                    GaleriaFotosUiState.Error("Erro ao carregar fotos: ${e.message}")
                }
            }
        }
    }
}
