package br.com.shubudo.ui.uistate

import br.com.shubudo.model.Usuario

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val usuario: Usuario) : LoginUiState()
    data class Error(val mensagem: String) : LoginUiState()
    data class NavigateToConfirmEmail(
        val username: String,
        val corFaixa: String
    ) : LoginUiState()

}


