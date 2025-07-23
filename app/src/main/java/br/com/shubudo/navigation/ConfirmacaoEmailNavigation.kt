package br.com.shubudo.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.novoUsuario.ConfirmacaoEmailView
import br.com.shubudo.ui.viewModel.ConfirmacaoEmailViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel

internal const val confirmacaoEmailRoute = "confirmacao_email"
internal const val confirmacaoEmailArgument = "email"
internal const val confirmacaoSenhaArgument = "senha"
internal const val confirmacaoCorFaixaArgument = "corFaixa"
internal const val confirmacaoEmailFullPath =
    "$confirmacaoEmailRoute/{$confirmacaoEmailArgument}/{$confirmacaoSenhaArgument}/{$confirmacaoCorFaixaArgument}"

fun NavGraphBuilder.confirmacaoEmailScreen(
    themeViewModel: ThemeViewModel,
    onConfirmado: () -> Unit,
    onBackToLogin: () -> Unit
) {
    composable(route = confirmacaoEmailFullPath) { backStackEntry ->
        val email = backStackEntry.arguments?.getString(confirmacaoEmailArgument) ?: ""
        val senha = backStackEntry.arguments?.getString(confirmacaoSenhaArgument) ?: ""
        val corFaixa = backStackEntry.arguments?.getString(confirmacaoCorFaixaArgument) ?: ""
        val viewModel = hiltViewModel<ConfirmacaoEmailViewModel>()

        // Mantém o tema da faixa selecionada durante o cadastro
        LaunchedEffect(corFaixa) {
            if (corFaixa.isNotBlank() && corFaixa != "branca") {
                themeViewModel.changeThemeFaixa(corFaixa)
            }
        }

        ConfirmacaoEmailView(
            email = email,
            senha = senha,
            viewModel = viewModel,
            onConfirmado = onConfirmado,
            onBackToLogin = onBackToLogin,
            themeViewModel = themeViewModel
        )
    }
}

fun NavController.navigateToConfirmacaoEmail(
    email: String,
    senha: String,
    corFaixa: String,
    navOptions: NavOptions? = null
) {
    navigate("$confirmacaoEmailRoute/$email/$senha/$corFaixa", navOptions)
}
