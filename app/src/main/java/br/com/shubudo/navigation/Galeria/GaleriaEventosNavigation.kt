package br.com.shubudo.navigation.Galeria

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.GaleriaEventosView

const val galeriaEventosRoute = "galeria/eventos"

fun NavGraphBuilder.galeriaEventosScreen(
    onEventoClick: (String) -> Unit,
    onEditarClick: (String) -> Unit = {},
    onCadastrarClick: () -> Unit
) {
    composable(route = galeriaEventosRoute) {
        GaleriaEventosView(
            onAddEventoClick = onCadastrarClick,
            onEditEventoClick = onEditarClick,
            onClickEvento = onEventoClick
        )
    }
}

fun NavController.navigateToGaleriaEventos(navOptions: NavOptions? = null) {
    navigate(galeriaEventosRoute, navOptions)
}

