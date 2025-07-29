package br.com.shubudo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.recursos.academias.AcademiasView

const val academiasRoute = "academias"

fun NavGraphBuilder.academiasScreen(
    onAddAcademiaClick: () -> Unit = {},
    onEditAcademiaClick: (String) -> Unit = {}
) {
    composable(
        route = academiasRoute
    ) {
        AcademiasView(
            onNavigateToEditarAcademia = onEditAcademiaClick,
            onNavigateToCadastroAcademia = onAddAcademiaClick,
        )
    }
}

fun NavController.navigateToAcademias(
    navOptions: NavOptions? = null,
) {
    navigate(academiasRoute, navOptions)
}
