package br.com.shubudo.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.novoUsuario.NovoUsuarioView
import br.com.shubudo.ui.viewModel.DropDownMenuViewModel
import br.com.shubudo.ui.viewModel.ProgramacaoViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel

internal const val novoUsuarioRote = "novoUsuario/{username}"
internal const val novoUsuarioRoteSemUsername = "novoUsuario" // Sem argumento


fun NavGraphBuilder.novoUsuarioScreen(
    themeViewModel: ThemeViewModel,
    dropDownMenuViewModel: DropDownMenuViewModel,
    onNavigateToLogin: (String) -> Unit,
) {
    composable(novoUsuarioRote) { backStackEntry ->
        val viewModel = hiltViewModel<ProgramacaoViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        NovoUsuarioView(
            themeViewModel = themeViewModel,
            onNavigateToLogin = onNavigateToLogin,
            dropDownMenuViewModel = dropDownMenuViewModel,
            username = backStackEntry.arguments?.getString("username") ?: "",
        )
    }

    // Rota alternativa sem username
    composable(novoUsuarioRoteSemUsername) {
        NovoUsuarioView(
            themeViewModel = themeViewModel,
            onNavigateToLogin = onNavigateToLogin,
            dropDownMenuViewModel = dropDownMenuViewModel,
            username = "" // Passa login vazio
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