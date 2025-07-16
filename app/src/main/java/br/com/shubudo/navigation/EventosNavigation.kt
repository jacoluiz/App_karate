package br.com.shubudo.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.EventosView
import br.com.shubudo.ui.viewModel.EventosViewModel

internal const val eventosRoute = "eventos"

fun NavGraphBuilder.eventosScreen(
    onReload: () -> Unit,
    onEventClick: (String) -> Unit
) {
    composable(eventosRoute) {
        val eventosViewModel: EventosViewModel = hiltViewModel()
        val uiState by eventosViewModel.eventoUiState.collectAsState()

        EventosView(
            uiState = uiState,
            onReload = {
                eventosViewModel.recarregarEventos()
                onReload()
            },
            onAddEventoClick = {},
            onEventClick = onEventClick
            onEventClick = onEventClick
        )
    }
}

fun NavController.navigateToEventos(
    navOptions: NavOptions? = null,
) {
    navigate(eventosRoute, navOptions)
}
