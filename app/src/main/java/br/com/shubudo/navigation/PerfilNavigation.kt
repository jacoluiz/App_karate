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
import br.com.shubudo.ui.viewModel.components.ThemeViewModel


internal const val perfilRoute = "perfil"

fun NavGraphBuilder.perfilScreen(
    onLogoutNavegacao: () -> Unit, // callback que navega para Login após logout
    onEditarPerfil: () -> Unit,
    themeViewModel: ThemeViewModel
) {
    composable(perfilRoute) {
        val viewModel = hiltViewModel<PerfilViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        PerfilView(
            uiState = uiState,
            themeViewModel = themeViewModel,
            onEditarPerfil = { onEditarPerfil() },
            onLogout = {
                viewModel.logout {
                    onLogoutNavegacao() // após deletar usuário, navega
                }
            }
        )
    }
}


fun NavController.navigateToPerfil(
    navOptions: NavOptions? = null
) {
    navigate(perfilRoute, navOptions)
}