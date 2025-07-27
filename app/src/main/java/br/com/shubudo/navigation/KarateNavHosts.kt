package br.com.shubudo.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import br.com.shubudo.navigation.Galeria.galeriaEventosCadastrarScreen
import br.com.shubudo.navigation.Galeria.galeriaEventosScreen

import br.com.shubudo.navigation.Galeria.galeriaFotosScreen
import br.com.shubudo.navigation.Galeria.navigateToGaleriaEventos
import br.com.shubudo.navigation.Galeria.navigateToGaleriaEventosCadastrar

import br.com.shubudo.navigation.Galeria.navigateToGaleriaFotos
import br.com.shubudo.ui.viewModel.DropDownMenuViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel

@Composable
fun KarateNavHost(
    navController: NavHostController = rememberNavController(),
    dropDownMenuViewModel: DropDownMenuViewModel,
    themeViewModel: ThemeViewModel
) {

    NavHost(navController = navController,
        startDestination = AppDestination.Recursos.route,
        enterTransition = {
            when (targetState.destination.route) {
                AppDestination.Recursos.route -> slideInHorizontally(
                    initialOffsetX = { -it }, // entra pela esquerda
                    animationSpec = tween(500)
                ) + fadeIn()

                else -> slideInHorizontally(
                    initialOffsetX = { it }, // entra pela direita
                    animationSpec = tween(500)
                ) + fadeIn()
            }
        },
        exitTransition = {
            when (initialState.destination.route) {
                AppDestination.Recursos.route -> slideOutHorizontally(
                    targetOffsetX = { -it }, // sai pela esquerda
                    animationSpec = tween(1500)
                ) + fadeOut()

                else -> slideOutHorizontally(
                    targetOffsetX = { it }, // sai pela direita
                    animationSpec = tween(1500)
                ) + fadeOut()
            }
        },
        popEnterTransition = {
            when (targetState.destination.route) {
                AppDestination.Recursos.route -> slideInHorizontally(
                    initialOffsetX = { it }, // volta da direita
                    animationSpec = tween(500)
                ) + fadeIn()

                else -> slideInHorizontally(
                    initialOffsetX = { -it }, // volta da esquerda
                    animationSpec = tween(500)
                ) + fadeIn()
            }
        },
        popExitTransition = {
            when (initialState.destination.route) {
                AppDestination.Recursos.route -> slideOutHorizontally(
                    targetOffsetX = { it }, // sai pela direita
                    animationSpec = tween(1500)
                ) + fadeOut()

                else -> slideOutHorizontally(
                    targetOffsetX = { -it }, // sai pela esquerda
                    animationSpec = tween(1500)
                ) + fadeOut()
            }
        }

    ) {
        // Tela de Login
        loginScreen(themeViewModel = themeViewModel, onNavigateToNovoUsuario = { username ->
            navController.navigateToNovoUsuario(username.toString())
        }, onNavigateToHome = {
            navController.navigateToRecursos()
        }, onNavigateToEsqueciMinhaSenha = {
            navController.navigateToEsqueciMinhaSenha()
        }, onNavigateToConfirmEmail = { email, senha, corFaixa ->
            navController.navigateToConfirmacaoEmail(email, senha, corFaixa)
        })

        // Tela de Eventos
        eventosScreen(onReload = { navController.navigate(eventosRoute) },
            onEventClick = { eventoId ->
                navController.navigateToEventoDetalhe(eventoId)
            },
            onAddEventoClick = { navController.navigateToCadastroEvento() },
            onEditEventoClick = { eventoId -> navController.navigateToCadastroEvento(eventoId) })

        // Tela de Perfil
        perfilScreen(themeViewModel = themeViewModel, onLogoutNavegacao = {
            navController.navigate(AppDestination.Login.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }, onEditarPerfil = { navController.navigateToEditarPerfil() })

        // Tela de Confirmação de E-mail
        confirmacaoEmailScreen(themeViewModel = themeViewModel, onConfirmado = {
            // Navegar para a tela principal após confirmação bem-sucedida
            navController.navigateToRecursos()
        }, onBackToLogin = {
            // Navegar para login quando há falha nas credenciais
            navController.navigate(AppDestination.Login.route) {
                popUpTo(confirmacaoEmailRoute) { inclusive = true }
                launchSingleTop = true
            }
        })

        // Tela de Esqueci Minha Senha
        esqueciMinhaSenhaScreen(onSenhaRedefinida = {
            navController.navigate("login") {
                popUpTo("esqueciMinhaSenha") { inclusive = true }
            }
        })

        // Tela de Programação
        programacaoScreen(
            onNavigateToDetalheFaixa = { navController.navigateToDetalheFaixa(it) },
            themeViewModel = themeViewModel
        )

        // Tela de Detalhe da Faixa
        detalheFaixaScreen(onNavigateToDetalheMovimento = { faixa, movimento ->
            navController.navigateToDetalheMovimento(faixa, movimento)
        })

        // Tela de Detalhe do Movimento
        detalheMovimentoScreen(onBackNavigationClick = { navController.popBackStack() })

        // Tela de Novo Usuário
        novoUsuarioScreen(themeViewModel = themeViewModel,
            dropDownMenuViewModel = dropDownMenuViewModel,
            onNavigateToLogin = { email, senha, corFaixa ->
                navController.navigateToConfirmacaoEmail(email, senha, corFaixa)
            })

        // Tela de Editar Perfil
        editarPerfilScreen(
            themeViewModel = themeViewModel,
            onSaveSuccess = { navController.popBackStack() },
            onCancelar = { navController.popBackStack() },
            navController = navController
        )

        // Tela de Detalhe do Evento
        eventoDetalheScreen {
            navController.popBackStack()
        }

        // Tela de Recursos
        recursosScreen(
            onNavigateToAvisos = {
                navController.navigateToAvisos()
            }, onNavigateToEventos = {
                navController.navigateToEventos()
            }, onNavigateToProgramacao = {
                navController.navigateToProgramacao(navOptions {
                    launchSingleTop = true
                    popUpTo(programacaoRoute)
                })
            }, onNavigateToAcademias = {
                navController.navigateToAcademias()
            }, onNavigateToBaseUsuarios = {
                navController.navigateToBaseUsuarios()
            },
            onNavigateToGaleria = {
                navController.navigateToGaleriaEventos()
            }
        )

        // Tela de Galeria de Eventos
        galeriaEventosScreen(
            onEventoClick = { eventoId ->
                navController.navigateToGaleriaFotos(eventoId)
            },
            onCadastrarClick = {
                navController.navigateToGaleriaEventosCadastrar()
            },
            onEditarClick = { eventoId ->
                navController.navigateToGaleriaEventosCadastrar(eventoId)
            }
        )

        // Tela de Cadastro de Evento na Galeria
        galeriaEventosCadastrarScreen(
            navigationBack = {
                navController.popBackStack()
            }
        )

        // Tela de Galeria de Fotos
        galeriaFotosScreen(
            onVoltar = { navController.popBackStack() },
        )

        // Tela de Base de Usuários
        baseUsuariosScreen(
            navController = navController,
            onNavigateToEditarUsuario = { usuarioId ->
                navController.navigateToEditarUsuario(usuarioId)
            }
        )

        // Tela de Avisos
        avisosScreen(onNavigateToCadastroAviso = { navController.navigateToCadastroAviso() },
            onNavigateToEditarAviso = { avisoId ->
                navController.navigateToCadastroAviso(avisoId)
            })

        // Tela de Cadastro de Aviso
        cadastroAvisoScreen(onNavigateBack = {
            navController.popBackStack()
        })

        // Tela de Cadastro de Evento
        cadastroEventoScreen(onNavigateBack = {
            navController.popBackStack()
        })

        // Tela de Academias
        academiasScreen(onAddAcademiaClick = {
            navController.navigateToCadastroAcademia()
        }, onEditAcademiaClick = { academiaId ->
            navController.navigateToCadastroAcademia(academiaId)
        })

        // Tela de Cadastro de Academia
        cadastroAcademiaScreen(onNavigateBack = {
            navController.popBackStack()
        })
    }
}

