package br.com.shubudo.navigation

sealed class AppDestination(val route: String) {
    object Recursos : AppDestination("recursos")
    object Perfil : AppDestination("perfil")
    object Login : AppDestination("login")

}