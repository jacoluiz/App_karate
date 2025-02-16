package br.com.shubudo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.NovoAvisoView

// Define a rota para a tela de NovoAviso
internal const val novoAvisoRoute = "novoAviso"

// Registra a tela de NovoAviso no NavGraph
fun NavGraphBuilder.novoAvisoScreen(
    onSaveSuccess: () -> Unit,  // Callback disparado após salvar com sucesso
    onCancelar: () -> Unit       // Callback para cancelar a operação
) {
    composable(novoAvisoRoute) {
        // Obtém o ViewModel com Hilt
//        val viewModel = hiltViewModel<NovoAvisoViewModel>()
        // Coleta o estado da UI exposto pelo ViewModel
//        val uiState by viewModel.uiState.collectAsState()
        // Renderiza a tela de NovoAviso
        NovoAvisoView(
            onSave = { onSaveSuccess() },
            onCancel = { onCancelar() }
        )
    }
}

// Função de extensão para facilitar a navegação para a tela de NovoAviso
fun NavController.navigateToNovoAviso(
    navOptions: NavOptions? = null
) {
    navigate(novoAvisoRoute, navOptions)
}
