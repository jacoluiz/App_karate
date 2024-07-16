package br.com.shubudo.navigation

sealed class AppDestination(val route: String) {
    object Programacao : AppDestination("programacao")
    object Avisos : AppDestination("avisos")
    object Perfil : AppDestination("perfil")
    object DetalheFaixa : AppDestination("detalheFaixa")
}