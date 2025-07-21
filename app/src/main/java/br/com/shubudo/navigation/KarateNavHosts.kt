package br.com.shubudo.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import br.com.shubudo.ui.components.appBar.BottomAppBarItem
import br.com.shubudo.ui.viewModel.DropDownMenuViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel

@Composable
fun KarateNavHost(
    navController: NavHostController = rememberNavController(),
    dropDownMenuViewModel: DropDownMenuViewModel,
    themeViewModel: ThemeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Evento.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        // Tela de Login
        loginScreen(
            themeViewModel = themeViewModel,
            onNavigateToNovoUsuario = { username ->
                navController.navigateToNovoUsuario(username.toString())
            },
            onNavigateToHome = {
                navController.navigateToBottomAppBarItem(BottomAppBarItem.Eventos)
            },
            onNavigateToEsqueciMinhaSenha = {
                navController.navigateToEsqueciMinhaSenha()
            },
            onNavigateToConfirmEmail = { email, senha, corFaixa ->
                navController.navigateToConfirmacaoEmail(email, senha, corFaixa)
            }
        )

        // Tela de Eventos
        eventosScreen(
            onReload = { navController.navigate(eventosRoute) },
            onEventClick = { eventoId ->
                navController.navigateToEventoDetalhe(eventoId)
            }
        )

        // Tela de Perfil
        perfilScreen(
            themeViewModel = themeViewModel,
            onLogoutNavegacao = {
                navController.navigate(AppDestination.Login.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onEditarPerfil = { navController.navigateToEditarPerfil() }
        )

        // Tela de Confirmação de E-mail
        confirmacaoEmailScreen(
            themeViewModel = themeViewModel,
            onConfirmado = {
                // Navegar para a tela principal após confirmação bem-sucedida
                navController.navigateToBottomAppBarItem(BottomAppBarItem.Eventos)
            },
            onBackToLogin = {
                // Navegar para login quando há falha nas credenciais
                navController.navigate(AppDestination.Login.route) {
                    popUpTo(confirmacaoEmailRoute) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )

        // Tela de Esqueci Minha Senha
        esqueciMinhaSenhaScreen(
            onSenhaRedefinida = {
                navController.navigate("login") {
                    popUpTo("esqueciMinhaSenha") { inclusive = true }
                }
            }
        )

        confirmarNovaSenhaScreen(
            onSenhaAlterada = {
                navController.navigate("login") {
                    popUpTo(confirmarNovaSenhaRoute) { inclusive = true }
                }
            },
            onVoltar = {
                navController.navigateToEsqueciMinhaSenha()
            }
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
            onNavigateToLogin = { email, senha, corFaixa ->
                navController.navigateToConfirmacaoEmail(email, senha, corFaixa)
            }
        )

        // Tela de Editar Perfil
        editarPerfilScreen(
            themeViewModel = themeViewModel,
            onSaveSuccess = { navController.popBackStack() },
            onCancelar = { navController.popBackStack() }
        )

        // Tela de Detalhe do Evento
        eventoDetalheScreen {
            navController.popBackStack()
        }
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

        BottomAppBarItem.Perfil -> {
            navigate(perfilRoute) {
                popUpTo(perfilRoute) { inclusive = true }
                launchSingleTop = true
            }
        }

        BottomAppBarItem.Eventos -> {
            navigateToEventos(navOptions {
                launchSingleTop = true
                popUpTo(eventosRoute)
            })
        }
    }
}
