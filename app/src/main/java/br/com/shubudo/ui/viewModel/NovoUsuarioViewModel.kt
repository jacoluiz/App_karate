package br.com.shubudo.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NovoUsuarioViewModel : ViewModel() {
    var nome by mutableStateOf("")
    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    var confirmarSenha by mutableStateOf("")
    var faixa by mutableStateOf("")
    var peso by mutableStateOf("")
    var altura by  mutableStateOf("0,00")
}