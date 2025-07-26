package br.com.shubudo.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.EditarPerfilView
import br.com.shubudo.ui.viewModel.EditarPerfilViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel

// Rota para a tela de edição de perfil
internal const val editarPerfilRoute = "editarPerfil"

fun NavGraphBuilder.editarPerfilScreen(
    themeViewModel: ThemeViewModel,
    navController: NavController,
    onSaveSuccess: () -> Unit,  // Exemplo: callback ao salvar
    onCancelar: () -> Unit
) {
    composable(editarPerfilRoute) {
        val viewModel = hiltViewModel<EditarPerfilViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        EditarPerfilView(
            onSave = { onSaveSuccess() },
            uiState = uiState,
            themeViewModel = themeViewModel,
            onCancelar = { onCancelar() }
        )
    }

    // Composable para editar perfil com ID de usuário
    composable(
        route = "$editarPerfilRoute/{usuarioId}"
    ) { backStackEntry ->
        val usuarioId = backStackEntry.arguments?.getString("usuarioId") ?: return@composable
        val viewModel = hiltViewModel<EditarPerfilViewModel>()

        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(usuarioId) {
            viewModel.carregarUsuarioPorId(usuarioId)
        }

        EditarPerfilView(
            onSave = {
                navController.previousBackStackEntry?.savedStateHandle?.set("refreshUsuarios", true)
                navController.popBackStack()
            },
            uiState = uiState,
            themeViewModel = themeViewModel,
            onCancelar = { onCancelar() }
        )
    }

}

/**
 * Extensão para facilitar a navegação até a tela de edição de perfil
 */
fun NavController.navigateToEditarPerfil(
    navOptions: NavOptions? = null
) {
    navigate(editarPerfilRoute, navOptions)
}

/**
 * Extensão para facilitar a navegação até a tela de edição de usuário (modo admin)
 */
fun NavController.navigateToEditarUsuario(
    usuarioId: String,
    navOptions: NavOptions? = null
) {
    navigate("$editarPerfilRoute/$usuarioId", navOptions)
}