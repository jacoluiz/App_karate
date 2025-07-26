package br.com.shubudo.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.SessionManager.usuarioLogado
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ThemeViewModel : ViewModel() {

    private val availableFaixas = listOf(
        "Branca", "Amarela", "Laranja", "Verde", "Roxa", "Marrom", "Preta",
        "Mestre", "Grão Mestre"
    )

    var currentFaixa by mutableStateOf("Branca")
        private set

    init {
        viewModelScope.launch {
            delay(1000)
            updateFromSession()
        }
    }

    private fun updateFromSession() {
        currentFaixa = usuarioLogado?.corFaixa ?: availableFaixas.random()
    }

    fun changeThemeFaixa(faixa: String) {
        currentFaixa = faixa
    }
}
