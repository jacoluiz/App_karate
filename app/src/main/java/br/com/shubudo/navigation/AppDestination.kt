package br.com.shubudo.navigation

sealed class AppDestination(val route: String) {
    object Programacao : AppDestination("programacao")
    object Evento : AppDestination("eventos")
    object Perfil : AppDestination("perfil")
    object Login : AppDestination("login")

}