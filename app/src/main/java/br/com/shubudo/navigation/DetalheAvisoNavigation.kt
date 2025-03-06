package br.com.shubudo.navigation

import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.shubudo.ui.view.DetalheAvisoView
import br.com.shubudo.ui.viewModel.DetalheAvisoViewModel

// Define a rota com o parâmetro "avisoId"
internal const val detalheAvisoRoute = "detalheAviso/{$detalheAvisoArgument}"

// Na navegação, declare o argumento para que o framework o trate corretamente.
fun NavGraphBuilder.detalheAvisoScreen(
    onDelete: () -> Unit,
    onEdit: (Any?) -> Unit
) {
    composable(
        route = detalheAvisoRoute,
        arguments = listOf(navArgument("avisoId") { type = NavType.StringType })
    ) { backStackEntry ->
        // Obtém o argumento "avisoId" da rota.
        val avisoId = backStackEntry.arguments?.getString("avisoId")!!

        // Obtém o ViewModel com Hilt. Certifique-se de que seu ViewModel utiliza o SavedStateHandle para ler "avisoId".
        val viewModel: DetalheAvisoViewModel = hiltViewModel()

        // Coleta o estado da UI a partir do ViewModel.
        val uiState = viewModel.uiState.collectAsState().value

        // Renderiza a view, passando o estado da UI.
        DetalheAvisoView(
            uiState = uiState,
            onNavigationPop = onDelete,
            onEdit = onEdit
        )
    }
}

// Função de extensão para navegar para a tela de detalhe do aviso, passando o ID como parâmetro.
fun NavController.navigateToDetalheAviso(
    avisoId: String,
    navOptions: NavOptions? = null
) {
    navigate("detalheAviso/$avisoId", navOptions)
}
