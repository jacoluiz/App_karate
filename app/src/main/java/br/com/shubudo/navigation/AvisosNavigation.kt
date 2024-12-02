package br.com.shubudo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.AvisosView


internal const val avisosRoute = "avisos"

fun NavGraphBuilder.avisosScreen(onClickAviso: () -> Unit) {
    composable(avisosRoute) {
        AvisosView(
            onClickAviso = onClickAviso
        )
    }
}

fun NavController.navigateToAvisos(
    navOptions: NavOptions? = null
) {
    navigate(avisosRoute, navOptions)
}