package br.com.shubudo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.shubudo.ui.view.CadastroAvisoView

const val cadastroAvisoRoute = "cadastro_aviso"
const val cadastroAvisoWithIdRoute = "cadastro_aviso/{avisoId}"

fun NavGraphBuilder.cadastroAvisoScreen(
    onNavigateBack: () -> Unit
) {
    // Tela para novo aviso
    composable(route = cadastroAvisoRoute) {
        CadastroAvisoView(
            avisoId = "",
            onNavigateBack = onNavigateBack
        )
    }

    // Tela para editar aviso
    composable(
        route = cadastroAvisoWithIdRoute,
        arguments = listOf(navArgument("avisoId") { type = NavType.StringType })
    ) { backStackEntry ->
        val avisoId = backStackEntry.arguments?.getString("avisoId") ?: ""
        CadastroAvisoView(
            avisoId = avisoId,
            onNavigateBack = onNavigateBack
        )
    }
}

fun NavController.navigateToCadastroAviso(navOptions: NavOptions? = null) {
    navigate("cadastro_aviso", navOptions)
}

fun NavController.navigateToCadastroAviso(avisoId: String, navOptions: NavOptions? = null) {
    navigate("cadastro_aviso/$avisoId", navOptions)
}

