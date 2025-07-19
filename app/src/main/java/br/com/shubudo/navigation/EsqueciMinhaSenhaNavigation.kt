package br.com.shubudo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.shubudo.ui.view.EsqueciMinhaSenhaView

internal const val esqueciMinhaSenhaRote = "esqueciMinhaSenha/{username}"
internal const val esqueciMinhaSenhaRoteSemUsername = "esqueciMinhaSenha" // Sem argumento


fun NavGraphBuilder.esqueciMinhaSenhaScreen(
    onSendResetRequest: () -> Boolean,
    onSenhaRedefinida: () -> Unit
) {
    composable(esqueciMinhaSenhaRote) { backStackEntry ->
        EsqueciMinhaSenhaView(
            username = backStackEntry.arguments?.getString("username") ?: "",
            onSendResetRequest = onSendResetRequest,
            onSenhaRedefinida = onSenhaRedefinida
        )
    }

    composable(esqueciMinhaSenhaRoteSemUsername) {
        EsqueciMinhaSenhaView(
            username = "",
            onSendResetRequest = onSendResetRequest,
            onSenhaRedefinida = onSenhaRedefinida
        )
    }
}



fun NavController.navigateToEsqueciMinhaSenha(
    username: String = "",
    navOptions: NavOptions? = null
) {

    val route = if (username.isNotEmpty()) {
        "esqueciMinhaSenha/$username"
    } else {
        "esqueciMinhaSenha"
    }
    navigate(route, navOptions)
}