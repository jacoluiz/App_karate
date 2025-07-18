package br.com.shubudo.ui.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {

    private val availableFaixas = listOf(
        "Branca", "Amarela", "Laranja", "Verde", "Roxa", "Marrom", "Preta",
        "Mestre", "Grão Mestre"
    )

    private val _currentFaixa = mutableStateOf<String?>(null)

    fun changeThemeFaixa(faixa: String) {
        _currentFaixa.value = faixa
    }

    fun getFaixaAtualOuAleatoria(): String {
        return _currentFaixa.value ?: availableFaixas.random()
    }
}

