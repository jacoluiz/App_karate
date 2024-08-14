package br.com.shubudo.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.ProgramacaoView
import br.com.shubudo.ui.viewModel.ProgramacaoViewModel

internal const val programacaoRoute = "programacao"

fun NavGraphBuilder.programacaoScreen(
    changeFaixa: (String) -> Unit,
    onNavigateToDetalheFaixa: (String) -> Unit
) {
    composable(programacaoRoute) {
        val viewModel = hiltViewModel<ProgramacaoViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        ProgramacaoView(
            changeThemeFaixa = changeFaixa,
            uiState = uiState,
            onClickFaixa = onNavigateToDetalheFaixa
        )
    }
}

fun NavController.navigateToProgramacao(
    navOptions: NavOptions? = null
) {
    navigate(programacaoRoute, navOptions)
}