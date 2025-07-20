package br.com.shubudo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.LoginView
import br.com.shubudo.ui.viewModel.ThemeViewModel

internal const val loginRoute = "login"

fun NavGraphBuilder.loginScreen(
    themeViewModel: ThemeViewModel,
    onNavigateToHome: (String) -> Unit,
    onNavigateToNovoUsuario: (Any?) -> Unit,
    onNavigateToEsqueciMinhaSenha: (Any?) -> Unit,
    onNavigateToConfirmEmail: (String, String, String ) -> Unit
) {
    composable(loginRoute) {
        LoginView(
            themeViewModel = themeViewModel,
            onNavigateToHome = onNavigateToHome,
            onNavigateToNovoUsuario = onNavigateToNovoUsuario,
            onNavigateToEsqueciMinhaSenha = onNavigateToEsqueciMinhaSenha,
            onNavigateToConfirmEmail = onNavigateToConfirmEmail
        )
    }
}

fun NavController.loginScreen(
    navOptions: NavOptions? = null
) {
    navigate(loginRoute, navOptions)
}