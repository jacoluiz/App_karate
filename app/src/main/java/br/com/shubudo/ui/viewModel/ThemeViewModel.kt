package br.com.shubudo.ui.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    private val _currentFaixa = mutableStateOf("Branca")
    val currentFaixa: State<String> = _currentFaixa

    fun changeThemeFaixa(faixa: String) {
       Log.d("ThemeViewModel", "changeThemeFaixa: $faixa")
        _currentFaixa.value = faixa
    }
}