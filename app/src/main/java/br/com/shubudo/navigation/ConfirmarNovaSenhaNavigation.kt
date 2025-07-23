package br.com.shubudo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.esqueciMinhaSenha.ConfirmarNovaSenhaView

const val confirmarNovaSenhaRoute = "confirmarNovaSenha"

fun NavGraphBuilder.confirmarNovaSenhaScreen(
    onSenhaAlterada: () -> Unit,
    onVoltar: () -> Unit
) {
    composable(confirmarNovaSenhaRoute) {
        ConfirmarNovaSenhaView(
            onSenhaAlterada = onSenhaAlterada,
            onVoltar = onVoltar
        )
    }
}

fun NavController.navigateToConfirmarNovaSenha(navOptions: NavOptions? = null) {
    navigate(confirmarNovaSenhaRoute, navOptions)
}
