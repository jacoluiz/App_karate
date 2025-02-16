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
import br.com.shubudo.ui.viewModel.ThemeViewModel


internal const val perfilRoute = "perfil"

fun NavGraphBuilder.perfilScreen(
    onLogout: () -> Unit,
    onEditarPerfil: () -> Unit,
    themeViewModel: ThemeViewModel
) {
    composable(perfilRoute) {
        val viewModel = hiltViewModel<PerfilViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        PerfilView(
            uiState = uiState,
            onEditarPerfil = { onEditarPerfil() },
            onLogout = { onLogout() },
            themeViewModel = themeViewModel
        )
    }
}

fun NavController.navigateToPerfil(
    navOptions: NavOptions? = null
) {
    navigate(perfilRoute, navOptions)
}