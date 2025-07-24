package br.com.shubudo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.CadastroEventoView

// Rota da tela de cadastro de evento
const val cadastroEventoRoute = "cadastro_evento"
const val cadastroEventoRouteWithArgs = "cadastro_evento?eventoId={eventoId}"

// Tela de cadastro/edição de evento
fun NavGraphBuilder.cadastroEventoScreen(
    onNavigateBack: () -> Unit = {}
) {
    composable(
        route = cadastroEventoRouteWithArgs
    ) { backStackEntry ->
        val eventoId = backStackEntry.arguments?.getString("eventoId")
        CadastroEventoView(
            eventoId = eventoId,
            onNavigateBack = onNavigateBack
        )
    }
}

// Função para navegar até a tela de cadastro de evento
fun NavController.navigateToCadastroEvento(eventoId: String? = null, navOptions: NavOptions? = null) {
    val route = if (eventoId.isNullOrBlank()) {
        "cadastro_evento"
    } else {
        "cadastro_evento?eventoId=$eventoId"
    }
    navigate(route, navOptions)
}