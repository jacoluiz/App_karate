package br.com.shubudo.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.PerfilView
import br.com.shubudo.ui.viewModel.PerfilViewModel


internal const val perfilRoute = "perfil"

fun NavGraphBuilder.perfilScreen(onLogout: () -> Unit) {
    composable(perfilRoute) {
        val viewModel = hiltViewModel<PerfilViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        PerfilView(
            uiState = uiState,
            onLogout = { onLogout() }
        )
    }
}

fun NavController.navigateToPerfil(
    navOptions: NavOptions? = null
) {
    navigate(perfilRoute, navOptions)
}