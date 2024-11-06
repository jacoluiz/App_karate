package br.com.shubudo.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.LoginView
import br.com.shubudo.ui.viewModel.ProgramacaoViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel


internal const val loginRoute = "login"

fun NavGraphBuilder.loginScreen(
    themeViewModel: ThemeViewModel,
    onNavigateToHome: (String) -> Unit,
    onNavigateToNovoUsuario: (Any?) -> Unit,
    onNavigateToEsqueciMinhaSenha: (Any?) -> Unit
) {
    composable(loginRoute) {
        val viewModel = hiltViewModel<ProgramacaoViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        LoginView(
            themeViewModel = themeViewModel,
            onNavigateToHome = onNavigateToHome,
            onNavigateToNovoUsuario = onNavigateToNovoUsuario,
            onNavigateToEsqueciMinhaSenha = onNavigateToEsqueciMinhaSenha
        )
    }
}

fun NavController.loginScreen(
    navOptions: NavOptions? = null
) {
    navigate(loginRoute, navOptions)
}