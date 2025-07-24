package br.com.shubudo.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.EventosView
import br.com.shubudo.ui.viewModel.EventoViewModel

internal const val eventosRoute = "eventos"

fun NavGraphBuilder.eventosScreen(
    onReload: () -> Unit,
    onEventClick: (String) -> Unit,
    onAddEventoClick: () -> Unit = {},
    onEditEventoClick: (String) -> Unit = {}
) {
    composable(
        route = eventosRoute,
        enterTransition = { null },
        exitTransition = { null },
        popEnterTransition = { null },
        popExitTransition = { null }
    ) {
        val viewModel = hiltViewModel<EventoViewModel>()
        val uiState by viewModel.eventosUiState.collectAsState()

        EventosView(
            uiState = uiState,
            onReload = { viewModel.recarregarEventos() },
            onEventClick = onEventClick,
            onAddEventoClick = onAddEventoClick,
            onEditEventoClick = onEditEventoClick,
            onDeleteEvento = { eventoId -> viewModel.deletarEvento(eventoId) }
        )
    }
}

fun NavController.navigateToEventos(
    navOptions: NavOptions? = null,
) {
    navigate(eventosRoute, navOptions)
}