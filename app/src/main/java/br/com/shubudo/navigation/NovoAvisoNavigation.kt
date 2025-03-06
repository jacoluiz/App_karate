package br.com.shubudo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.shubudo.ui.view.NovoOuEditarAvisoView

// 🔹 Rotas separadas para criar e editar
internal const val novoAvisoRoute = "novoAviso"
internal const val editarAvisoRoute = "editarAviso/{avisoId}"

/**
 * Adiciona as telas de criação e edição de avisos no NavGraph
 */
fun NavGraphBuilder.novoAvisoScreen(
    onSaveSuccess: () -> Unit,
    onCancel: () -> Unit
) {
    // 🔹 Tela para criação de um novo aviso (sem avisoId)
    composable(novoAvisoRoute) {
        NovoOuEditarAvisoView(
            avisoId = null,
            onSave = { onSaveSuccess() },
            onCancel = { onCancel() }
        )
    }

    // 🔹 Tela para edição de um aviso existente (passando avisoId)
    composable(
        route = editarAvisoRoute,
        arguments = listOf(navArgument("avisoId") { type = NavType.StringType })
    ) { backStackEntry ->
        val avisoId = backStackEntry.arguments?.getString("avisoId")!!
        NovoOuEditarAvisoView(
            avisoId = avisoId,
            onSave = { onSaveSuccess() },
            onCancel = { onCancel() }
        )
    }
}

/**
 * 🔹 Função para navegar até a tela de criação de um novo aviso
 */
fun NavController.navigateToNovoAviso(navOptions: NavOptions? = null) {
    navigate(novoAvisoRoute, navOptions)
}

/**
 * 🔹 Função para navegar até a tela de edição de um aviso existente
 */
fun NavController.navigateToEditarAviso(avisoId: String, navOptions: NavOptions? = null) {
    navigate("editarAviso/$avisoId", navOptions)
}
