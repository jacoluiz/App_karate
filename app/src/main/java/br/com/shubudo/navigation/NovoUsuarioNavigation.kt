package br.com.shubudo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.novoUsuario.NovoUsuarioView
import br.com.shubudo.ui.viewModel.components.DropDownMenuViewModel
import br.com.shubudo.ui.viewModel.components.ThemeViewModel

internal const val novoUsuarioRote = "novoUsuario/{username}"
internal const val novoUsuarioRoteSemUsername = "novoUsuario"

fun NavGraphBuilder.novoUsuarioScreen(
    themeViewModel: ThemeViewModel,
    dropDownMenuViewModel: DropDownMenuViewModel,
    onNavigateToLogin: (String, String, String) -> Unit,
) {
    composable(novoUsuarioRote) { backStackEntry ->
        NovoUsuarioView(
            themeViewModel = themeViewModel,
            onNavigateToLogin = onNavigateToLogin,
        )
    }

    composable(novoUsuarioRoteSemUsername) {
        NovoUsuarioView(
            themeViewModel = themeViewModel,
            onNavigateToLogin = onNavigateToLogin
        )
    }
}

fun NavController.navigateToNovoUsuario(
    username: String = "",
    navOptions: NavOptions? = null
) {
    val route = if (username.isNotEmpty()) {
        "novoUsuario/$username"
    } else {
        "novoUsuario"
    }
    navigate(route, navOptions)
}
