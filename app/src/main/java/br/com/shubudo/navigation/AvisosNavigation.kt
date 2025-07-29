package br.com.shubudo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.recursos.avisos.AvisosView

// Rota da tela de avisos
const val avisosRoute = "avisos"

// Tela de listagem de avisos
fun NavGraphBuilder.avisosScreen(
    onNavigateToCadastroAviso: (String) -> Unit = {},
    onNavigateToEditarAviso: (String) -> Unit = {}
) {
    composable(route = avisosRoute) {
        AvisosView(
            onNavigateToCadastroAviso = onNavigateToCadastroAviso,
            onNavigateToEditarAviso = onNavigateToEditarAviso
        )
    }
}

// Função para navegar até a tela de avisos
fun NavController.navigateToAvisos(navOptions: NavOptions? = null) {
    navigate(avisosRoute, navOptions)
}
