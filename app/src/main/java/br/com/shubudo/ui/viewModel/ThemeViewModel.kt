package br.com.shubudo.ui.viewModel

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class ThemeViewModel : ViewModel() {
    private val availableFaixas = listOf(
        "Branca", "Amarela", "Laranja", "Verde", "Roxa", "Marrom", "Preta",
        "Mestre", "Grão Mestre"
    )

    private val _currentFaixa =
        mutableStateOf(availableFaixas[Random.nextInt(availableFaixas.size)])
    val currentFaixa: State<String> = _currentFaixa

    fun changeThemeFaixa(faixa: String) {
        _currentFaixa.value = faixa
    }

    fun getFaixaTemaAtual(): String {
        return _currentFaixa.value
    }

    @Composable
    fun eTemaEscuro(): Boolean {
        return isSystemInDarkTheme()
    }
}