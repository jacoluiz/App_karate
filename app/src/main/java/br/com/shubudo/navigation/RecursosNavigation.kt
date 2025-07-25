package br.com.shubudo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.RecursosView

// Caminho da rota
const val recursosRoute = "recursos"

// Função que adiciona a composable no NavGraph
fun NavGraphBuilder.recursosScreen(
    onNavigateToAvisos: () -> Unit = {},
    onNavigateToEventos: () -> Unit = {},
    onNavigateToProgramacao: () -> Unit = {},
    onNavigateToAcademias: () -> Unit = {}
) {
    composable(
        route = recursosRoute,
        enterTransition = { null },
        exitTransition = { null },
        popEnterTransition = { null },
        popExitTransition = { null }
    ) {
        RecursosView(
            onNavigateToAvisos = onNavigateToAvisos,
            onNavigateToEventos = onNavigateToEventos,
            onNavigateToProgramacao = onNavigateToProgramacao,
            onNavigateToAcademias = onNavigateToAcademias
        )
    }
}

// Função de navegação reutilizável
fun NavController.navigateToRecursos(navOptions: NavOptions? = null) {
    navigate(recursosRoute, navOptions)
}
