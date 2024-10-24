package br.com.shubudo.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.DetalheMovimentoView.DetalheMovimentoView
import br.com.shubudo.ui.viewModel.DetalheMovimentoViewModel

internal const val detalheMovimentoArgument = "movimento"
internal const val detalheMovimentoRuteFullpath =
    "$detalherFaixaRoute/{$detalheFaixaArgument}/{$detalheMovimentoArgument}"

fun NavGraphBuilder.detalheMovimentoScreen(
    onBackNavigationClick: () -> Unit = {}
) {
    composable(detalheMovimentoRuteFullpath) { backStackEntry ->
        val faixa = backStackEntry.arguments?.getString(detalheFaixaArgument)
        val movimento = backStackEntry.arguments?.getString(detalheMovimentoArgument)

        if (faixa != null && movimento != null) {
            val viewModel = hiltViewModel<DetalheMovimentoViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            DetalheMovimentoView(
                uiState = uiState,
                onBackNavigationClick = onBackNavigationClick
            )
        }
    }
}

fun NavController.navigateToDetalheMovimento(
    faixa: String,
    movimento: String,
    navOptions: NavOptions? = null
) {
    navigate("$detalherFaixaRoute/$faixa/$movimento", navOptions)
}