package br.com.shubudo.navigation.parceiros

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.recursos.parceiros.ParceirosView

const val parceirosRoute = "parceiros"

fun NavGraphBuilder.parceirosScreen(
    onAddParceiroClick: () -> Unit = {},
    onEditParceiroClick: (String) -> Unit = {},
    onParceiroClick: (String) -> Unit = {},
) {
    composable(
        route = parceirosRoute
    ) {
        ParceirosView(
            onNavigateToEditarParceiro = onEditParceiroClick,
            onNavigateToCadastroParceiro = onAddParceiroClick,
            onNavigateToParceiroDetalhe = onParceiroClick
        )
    }
}

fun NavController.navigateToParceiros(
    navOptions: NavOptions? = null,
) {
    navigate(parceirosRoute, navOptions)
}
