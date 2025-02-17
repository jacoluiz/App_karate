package br.com.shubudo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import br.com.shubudo.model.Aviso
import br.com.shubudo.ui.components.appBar.BottomAppBarItem
import br.com.shubudo.ui.viewModel.DropDownMenuViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel

@Composable
fun KarateNavHost(
    navController: NavHostController = rememberNavController(),
    dropDownMenuViewModel: DropDownMenuViewModel,
    themeViewModel: ThemeViewModel
) {
    NavHost(navController, startDestination = AppDestination.Login.route) {
        avisosScreen(
            onReload = {
                navController.navigateToAvisos()
            },

            onAvisoClick = { aviso: Aviso ->
                navController.navigateToDetalheAviso(aviso._id)
            },

            onAddAvisoClick = {
                navController.navigateToNovoAviso()
            }
        )

        detalheAvisoScreen(
            onDelete = {
                navController.popBackStack()
            }
        )

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
            onEditarPerfil = {
                navController.navigateToEditarPerfil()
            }
        )

        esqueciMinhaSenhaScreen(
            onSendResetRequest = {
                navController.popBackStack()
            }
        )

        programacaoScreen(
            onNavigateToDetalheFaixa = { navController.navigateToDetalheFaixa(it) },
            themeViewModel = themeViewModel
        )

        detalheFaixaScreen(onNavigateToDetalheMovimento = { faixa, movimento ->
            navController.navigateToDetalheMovimento(faixa, movimento)
        })

        detalheMovimentoScreen(
            onBackNavigationClick = {
                navController.popBackStack()
            }
        )

        novoUsuarioScreen(
            themeViewModel = themeViewModel,
            dropDownMenuViewModel = dropDownMenuViewModel,
            onNavigateToLogin = {
                navController.popBackStack()
            }
        )

        loginScreen(
            themeViewModel = themeViewModel,
            onNavigateToNovoUsuario = { username ->
                navController.navigateToNovoUsuario(username.toString()) // Passa o valor de "username"
            },

            onNavigateToHome = {
                navController.navigateToBottomAppBarItem(BottomAppBarItem.Avisos)
            },

            onNavigateToEsqueciMinhaSenha = {
                navController.navigateToEsqueciMinhaSenha()
            }
        )

        editarPerfilScreen(
            themeViewModel = themeViewModel,
            onSaveSuccess = {

                navController.popBackStack()

            },
            onCancelar = {
                navController.popBackStack()
            }
        )

        novoAvisoScreen(
            onSaveSuccess = {
                navController.navigateToAvisos()
            },
            onCancelar = { navController.popBackStack() }
        )

    }
}

fun NavController.navigateToBottomAppBarItem(
    item: BottomAppBarItem,
) {
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