package br.com.shubudo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.shubudo.ui.view.recursos.academias.CadastroAcademiaView

const val cadastroAcademiaRoute = "cadastro_academia"
const val cadastroAcademiaWithIdRoute = "cadastro_academia/{academiaId}"

fun NavGraphBuilder.cadastroAcademiaScreen(
    onNavigateBack: () -> Unit
) {
    // Tela para nova academia
    composable(route = cadastroAcademiaRoute) {
        CadastroAcademiaView(
            academiaId = "",
            onNavigateBack = onNavigateBack
        )
    }

    // Tela para editar academia
    composable(
        route = cadastroAcademiaWithIdRoute,
        arguments = listOf(navArgument("academiaId") { type = NavType.StringType })
    ) { backStackEntry ->
        val academiaId = backStackEntry.arguments?.getString("academiaId") ?: ""
        CadastroAcademiaView(
            academiaId = academiaId,
            onNavigateBack = onNavigateBack
        )
    }
}

fun NavController.navigateToCadastroAcademia(navOptions: NavOptions? = null) {
    navigate(cadastroAcademiaRoute, navOptions)
}

fun NavController.navigateToCadastroAcademia(academiaId: String, navOptions: NavOptions? = null) {
    navigate("$cadastroAcademiaRoute/$academiaId", navOptions)
}
