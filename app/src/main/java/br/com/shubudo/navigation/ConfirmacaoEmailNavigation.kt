package br.com.shubudo.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.novoUsuario.ConfirmacaoEmailView
import br.com.shubudo.ui.viewModel.ConfirmacaoEmailViewModel

internal const val confirmacaoEmailRoute = "confirmacao_email"
internal const val confirmacaoEmailArgument = "email"
internal const val confirmacaoEmailFullPath = "$confirmacaoEmailRoute/{$confirmacaoEmailArgument}"

fun NavGraphBuilder.confirmacaoEmailScreen(
    onConfirmado: () -> Unit
) {
    composable(route = confirmacaoEmailFullPath) { backStackEntry ->
        val email = backStackEntry.arguments?.getString(confirmacaoEmailArgument) ?: ""
        val viewModel = hiltViewModel<ConfirmacaoEmailViewModel>()
        ConfirmacaoEmailView(
            email = email,
            viewModel = viewModel,
            onConfirmado = onConfirmado
        )
    }
}

fun NavController.navigateToConfirmacaoEmail(
    email: String,
    navOptions: NavOptions? = null
) {
    navigate("$confirmacaoEmailRoute/$email", navOptions)
}
