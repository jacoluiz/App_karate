package br.com.shubudo.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.auth.CognitoAuthManager
import br.com.shubudo.ui.uistate.ConfirmacaoEmailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmacaoEmailViewModel @Inject constructor(
    private val cognito: CognitoAuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConfirmacaoEmailUiState>(ConfirmacaoEmailUiState.Idle)
    val uiState: StateFlow<ConfirmacaoEmailUiState> = _uiState

    var codigo = ""
    var email = ""

    fun confirmarCodigo() {
        viewModelScope.launch {
            _uiState.value = ConfirmacaoEmailUiState.Loading
            cognito.confirmCode(email, codigo) { sucesso, erro ->
                if (sucesso) {
                    _uiState.value = ConfirmacaoEmailUiState.Success("Email confirmado com sucesso!")
                } else {
                    _uiState.value = ConfirmacaoEmailUiState.Error(erro ?: "Erro ao confirmar email.")
                }
            }
        }
    }
}
