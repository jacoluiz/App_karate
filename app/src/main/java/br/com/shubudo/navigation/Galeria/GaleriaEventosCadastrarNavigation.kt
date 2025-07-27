package br.com.shubudo.navigation.Galeria

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.shubudo.ui.view.GaleriaEventosCadastrarView

const val galeriaEventosCadastrarRoute = "galeria/eventos/cadastrar"
private const val eventoIdArg = "eventoId"

fun NavGraphBuilder.galeriaEventosCadastrarScreen(
    navigationBack: () -> Unit
) {
    composable(
        route = "$galeriaEventosCadastrarRoute/{$eventoIdArg}",
        arguments = listOf(navArgument(eventoIdArg) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) { backStackEntry ->
        val eventoId = backStackEntry.arguments?.getString(eventoIdArg)
        GaleriaEventosCadastrarView(
            eventoId = eventoId,
            navigationBack = navigationBack
        )
    }
}

fun NavController.navigateToGaleriaEventosCadastrar(
    eventoId: String? = null,
    navOptions: NavOptions? = null
) {
    val route = if (eventoId != null) {
        "$galeriaEventosCadastrarRoute/$eventoId"
    } else {
        "$galeriaEventosCadastrarRoute/null"
    }
    navigate(route, navOptions)
}
