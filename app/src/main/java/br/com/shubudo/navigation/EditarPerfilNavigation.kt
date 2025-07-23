package br.com.shubudo.navigation

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
}

/**
 * Extensão para facilitar a navegação até a tela de edição de perfil
 */
fun NavController.navigateToEditarPerfil(
    navOptions: NavOptions? = null
) {
    navigate(editarPerfilRoute, navOptions)
}