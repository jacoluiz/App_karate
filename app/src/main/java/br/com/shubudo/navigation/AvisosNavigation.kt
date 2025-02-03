package br.com.shubudo.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.AvisosView
import br.com.shubudo.ui.viewModel.AvisosViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel


internal const val avisosRoute = "avisos"

fun NavGraphBuilder.avisosScreen(
    themeViewModel: ThemeViewModel
) {

    composable(avisosRoute) {
        // Obtendo o AvisosViewModel
        val avisosViewModel: AvisosViewModel = hiltViewModel()

        // Renderizando a tela de avisos
        AvisosView(
            avisosViewModel = avisosViewModel, // Passa o ViewModel para a tela
            themeViewModel = themeViewModel, // Passa o ViewModel de tema para a tela
        )
    }
}

fun NavController.navigateToAvisos(
    navOptions: NavOptions? = null,
) {
    navigate(avisosRoute, navOptions)
}