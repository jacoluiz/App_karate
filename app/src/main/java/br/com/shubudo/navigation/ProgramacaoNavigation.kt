package br.com.shubudo.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.recursos.programacao.ProgramacaoView
import br.com.shubudo.ui.viewModel.ProgramacaoViewModel
import br.com.shubudo.ui.viewModel.components.ThemeViewModel

internal const val programacaoRoute = "programacao"

fun NavGraphBuilder.programacaoScreen(
    onNavigateToDetalheFaixa: (String) -> Unit,
    themeViewModel: ThemeViewModel
) {
    composable(programacaoRoute) {
        val viewModel = hiltViewModel<ProgramacaoViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        ProgramacaoView(
            uiState = uiState,
            onClickFaixa = onNavigateToDetalheFaixa,
            themeViewModel = themeViewModel
        )
    }
}

fun NavController.navigateToProgramacao(
    navOptions: NavOptions? = null,
) {
    navigate(programacaoRoute, navOptions )
}