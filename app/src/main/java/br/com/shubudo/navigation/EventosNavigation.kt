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
        val viewModel = hiltViewModel<EventosViewModel>()
        val uiState by viewModel.eventoUiState.collectAsState()

        EventosView(
            uiState = uiState,
            onReload = { viewModel.recarregarEventos() },
            onEventClick = onEventClick
        )
    }
}

fun NavController.navigateToEventos(
    navOptions: NavOptions? = null,
) {
    navigate(eventosRoute, navOptions)
}