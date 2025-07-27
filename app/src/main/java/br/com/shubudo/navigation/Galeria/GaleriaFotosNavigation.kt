package br.com.shubudo.navigation.Galeria

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.GaleriaFotosView

const val galeriaFotosRoute = "galeria/fotos/{eventoId}"

fun NavGraphBuilder.galeriaFotosScreen(
    onVoltar: () -> Unit = {}
) {
    composable(route = galeriaFotosRoute) { backStackEntry ->
        val eventoId = backStackEntry.arguments?.getString("eventoId") ?: return@composable
        GaleriaFotosView(
            eventoId = eventoId,
            onBack = onVoltar
        )
    }
}

fun NavController.navigateToGaleriaFotos(
    eventoId: String,
    navOptions: NavOptions? = null
) {
    navigate("galeria/fotos/$eventoId", navOptions)
}
