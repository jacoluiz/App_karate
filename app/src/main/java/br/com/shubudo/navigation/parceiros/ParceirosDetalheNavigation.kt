package br.com.shubudo.navigation.parceiros

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.recursos.parceiros.ParceiroDetalheView
import br.com.shubudo.ui.viewModel.ParceiroDetalheViewModel

const val parceiroDetalheRoute = "parceiro_detalhe"
const val parceiroDetalheArgId = "parceiroId"
const val parceiroDetalheFullRoute = "$parceiroDetalheRoute/{$parceiroDetalheArgId}"

fun NavGraphBuilder.parceiroDetalheScreen() {
    composable(
        route = parceiroDetalheFullRoute
    ) { backStackEntry ->
        val parceiroId = backStackEntry.arguments?.getString(parceiroDetalheArgId)
        val viewModel = hiltViewModel<ParceiroDetalheViewModel>()

        LaunchedEffect(parceiroId) {
            if (parceiroId != null) {
                viewModel.carregarParceiro(parceiroId)
            }
        }

        val uiState by viewModel.uiState.collectAsState()

        uiState.parceiro?.let { parceiro ->
            ParceiroDetalheView(parceiro = parceiro)
        }
    }
}

fun NavController.navigateToParceiroDetalhe(parceiroId: String, navOptions: NavOptions? = null) {
    navigate("$parceiroDetalheRoute/$parceiroId", navOptions)
}

