package br.com.shubudo.ui.uistate

import br.com.shubudo.model.GaleriaFoto

sealed class GaleriaFotosUiState {
    object Loading : GaleriaFotosUiState()
    data class Success(val fotos: List<GaleriaFoto>) : GaleriaFotosUiState()
    object Empty : GaleriaFotosUiState()
    data class Error(val mensagem: String) : GaleriaFotosUiState()
}
