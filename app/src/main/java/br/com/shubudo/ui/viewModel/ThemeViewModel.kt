package br.com.shubudo.ui.viewModel

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    private val _currentFaixa = mutableStateOf("Branca")
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