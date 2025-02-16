package br.com.shubudo.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.model.Aviso
import br.com.shubudo.ui.view.AvisosView
import br.com.shubudo.ui.viewModel.AvisosViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel

internal const val avisosRoute = "avisos"

fun NavGraphBuilder.avisosScreen(
    onAvisoClick: (Aviso) -> Unit,
    onAddAvisoClick: () -> Unit,
    onReload: () -> Unit
) {
    composable(avisosRoute) {
        // Obtém o AvisosViewModel injetado pelo Hilt
        val avisosViewModel: AvisosViewModel = hiltViewModel()

        // Coleta o uiState exposto pelo ViewModel
        val uiState by avisosViewModel.avisoUiState.collectAsState()

        // Renderiza a tela de avisos passando o uiState e as callbacks necessárias
        AvisosView(
            uiState = uiState,
            onAvisoClick = onAvisoClick,
            onAddAvisoClick = onAddAvisoClick,
            onReload = onReload,
        )
    }
}

fun NavController.navigateToAvisos(
    navOptions: NavOptions? = null,
) {
    navigate(avisosRoute, navOptions)
}
