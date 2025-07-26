package br.com.shubudo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.BaseDeUsuariosView

// Caminho da rota
const val baseUsuariosRoute = "base_usuarios"

// Função que adiciona a composable no NavGraph
fun NavGraphBuilder.baseUsuariosScreen(
    navController: NavController,
    onNavigateToEditarUsuario: (String) -> Unit = {}
) {
    composable(
        route = baseUsuariosRoute,
    ) {

        BaseDeUsuariosView(
            onNavigateToEditarUsuario = onNavigateToEditarUsuario,
            navController = navController
        )
    }
}

// Função de navegação reutilizável
fun NavController.navigateToBaseUsuarios(navOptions: NavOptions? = null) {
    navigate(baseUsuariosRoute, navOptions)
}
