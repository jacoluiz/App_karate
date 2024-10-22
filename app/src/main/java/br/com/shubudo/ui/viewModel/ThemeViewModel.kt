package br.com.shubudo.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    private val _currentFaixa = mutableStateOf("Roxa")
    val currentFaixa: State<String> = _currentFaixa

    fun changeThemeFaixa(faixa: String) {
        _currentFaixa.value = faixa
    }
}