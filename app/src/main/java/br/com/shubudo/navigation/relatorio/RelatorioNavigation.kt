package br.com.shubudo.navigation.relatorio

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.recursos.relatorio.RelatoriosView

const val relatoriosRoute = "relatorios"

fun NavGraphBuilder.relatoriosScreen(
) {
    composable(
        route = relatoriosRoute
    ) {
        RelatoriosView(
        )
    }
}

fun NavController.navigateToRelatorios(
    navOptions: NavOptions? = null
) {
    navigate(relatoriosRoute, navOptions)
}
