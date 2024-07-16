package br.com.shubudo.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.AvisosView
import br.com.shubudo.ui.viewModel.ProgramacaoViewModel


internal const val avisosRoute = "avisos"

fun NavGraphBuilder.avisosScreen() {
    composable(avisosRoute) {
        val viewModel = hiltViewModel<ProgramacaoViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        AvisosView(
        )
    }
}

fun NavController.navigateToAvisos(
    navOptions: NavOptions? = null
) {
    navigate(avisosRoute, navOptions)
}