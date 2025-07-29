package br.com.shubudo.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.recursos.programacao.DetalheFaixaView
import br.com.shubudo.ui.viewModel.DetalheFaixaViewModel

internal const val detalherFaixaRoute = "detalheFiaxa"
internal const val detalheFaixaArgument = "faixa"
internal const val detalheFaixaRuteFullpath = "$detalherFaixaRoute/{$detalheFaixaArgument}"

fun NavGraphBuilder.detalheFaixaScreen(
    onNavigateToDetalheMovimento: (String, String) -> Unit
) {
    composable(detalheFaixaRuteFullpath) { backStackEntry ->
        backStackEntry.arguments?.getString(detalheFaixaArgument)?.let {
            val viewModel = hiltViewModel<DetalheFaixaViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            DetalheFaixaView(
                uiState = uiState,
                onNavigateToDetalheMovimento = onNavigateToDetalheMovimento
            )
        }
    }
}

fun NavController.navigateToDetalheFaixa(
    faixa: String,
    navOptions: NavOptions? = null
) {
    navigate("$detalherFaixaRoute/$faixa", navOptions)
}