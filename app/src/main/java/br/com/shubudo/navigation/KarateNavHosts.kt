package br.com.shubudo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import br.com.shubudo.model.Aviso
import br.com.shubudo.ui.components.appBar.BottomAppBarItem
import br.com.shubudo.ui.viewModel.DropDownMenuViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel

@Composable
fun KarateNavHost(
    navController: androidx.navigation.NavHostController = rememberNavController(),
    dropDownMenuViewModel: DropDownMenuViewModel,
    themeViewModel: ThemeViewModel
) {
    NavHost(navController, startDestination = AppDestination.Login.route) {
        // Tela de Login
        loginScreen(
            themeViewModel = themeViewModel,
            onNavigateToNovoUsuario = { username ->
                navController.navigateToNovoUsuario(username.toString())
            },
            onNavigateToHome = {
                navController.navigateToBottomAppBarItem(BottomAppBarItem.Avisos)
            },
            onNavigateToEsqueciMinhaSenha = {
                navController.navigateToEsqueciMinhaSenha()
            }
        )

        // Tela de Avisos
        avisosScreen(
            onReload = { navController.navigateToAvisos() },
            onAvisoClick = { aviso: Aviso ->
                navController.navigateToDetalheAviso(aviso._id)
            },
            onAddAvisoClick = { navController.navigateToNovoAviso() }
        )

        // Tela de Detalhe do Aviso (com opções para deletar e editar)
        detalheAvisoScreen(
            onDelete = { navController.popBackStack() },
            onEdit = { avisoId ->
                navController.navigateToEditarAviso(avisoId.toString())
            }
        )

        // Tela de Perfil
        perfilScreen(
            themeViewModel = themeViewModel,
            onLogout = {
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onEditarPerfil = { navController.navigateToEditarPerfil() }
        )

        // Tela de Esqueci Minha Senha
        esqueciMinhaSenhaScreen(
            onSendResetRequest = { navController.popBackStack() }
        )

        // Tela de Programação
        programacaoScreen(
            onNavigateToDetalheFaixa = { navController.navigateToDetalheFaixa(it) },
            themeViewModel = themeViewModel
        )

        // Tela de Detalhe da Faixa
        detalheFaixaScreen(
            onNavigateToDetalheMovimento = { faixa, movimento ->
                navController.navigateToDetalheMovimento(faixa, movimento)
            }
        )

        // Tela de Detalhe do Movimento
        detalheMovimentoScreen(
            onBackNavigationClick = { navController.popBackStack() }
        )

        // Tela de Novo Usuário
        novoUsuarioScreen(
            themeViewModel = themeViewModel,
            dropDownMenuViewModel = dropDownMenuViewModel,
            onNavigateToLogin = { navController.popBackStack() }
        )

        // Tela de Editar Perfil
        editarPerfilScreen(
            themeViewModel = themeViewModel,
            onSaveSuccess = { navController.popBackStack() },
            onCancelar = { navController.popBackStack() }
        )

        // Tela de Criar ou Editar Aviso
        novoAvisoScreen(
            onSaveSuccess = { navController.navigateToAvisos() },
            onCancel = { navController.popBackStack() }
        )
    }
}

// Navegação para a tela correta dependendo do item da BottomBar
fun NavController.navigateToBottomAppBarItem(item: BottomAppBarItem) {
    when (item) {
        BottomAppBarItem.Conteudo -> {
            navigateToProgramacao(navOptions {
                launchSingleTop = true
                popUpTo(programacaoRoute)
            })
        }
        BottomAppBarItem.Avisos -> {
            navigateToAvisos(navOptions {
                launchSingleTop = true
                popUpTo(avisosRoute)
            })
        }
        BottomAppBarItem.Perfil -> {
            navigateToPerfil(navOptions {
                launchSingleTop = true
                popUpTo(perfilRoute)
            })
        }
    }
}
