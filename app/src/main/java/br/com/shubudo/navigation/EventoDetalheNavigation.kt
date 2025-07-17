package br.com.shubudo.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.EventoDetalheView
import br.com.shubudo.ui.viewModel.EventoDetalheViewModel

internal const val eventoDetalheRoute = "eventoDetalhe"
internal const val eventoIdArgument = "eventoId"
internal const val eventoDetalheRouteWithArgs = "$eventoDetalheRoute/{$eventoIdArgument}"

fun NavGraphBuilder.eventoDetalheScreen(
    onBackClick: () -> Unit
) {
    composable(route = eventoDetalheRouteWithArgs) { backStackEntry ->
        val eventoId = backStackEntry.arguments?.getString(eventoIdArgument)
        val viewModel = hiltViewModel<EventoDetalheViewModel>()
        
        // Load the event when the screen is first displayed
        LaunchedEffect(eventoId) {
            if (eventoId != null) {
                viewModel.loadEvento(eventoId)
            }
        }
        
        val uiState by viewModel.uiState.collectAsState()
        
        // Only show the detail view if we have a valid event
        uiState.evento?.let { evento ->
            EventoDetalheView(
                evento = evento,
                onBackClick = onBackClick
            )
        }
    }
}

fun NavController.navigateToEventoDetalhe(
    eventoId: String,
    navOptions: NavOptions? = null
) {
    navigate("$eventoDetalheRoute/$eventoId", navOptions)
}