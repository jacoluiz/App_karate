package br.com.shubudo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import br.com.shubudo.ui.components.appBar.BottomAppBarItem

@Composable
fun KarateNavHost(
    changeThemeFaixa: (String) -> Unit,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController, startDestination = avisosRoute) {
        programacaoScreen(onNavigateToDetalheFaixa = {
            navController.navigateToDetalheFaixa(it)
        }, changeFaixa = changeThemeFaixa)
        avisosScreen()
        perfilScreen()
        detalheFaixaScreen(onNavigateToDetalheMovimento = { faixa, movimento ->
            navController.navigateToDetalheMovimento(faixa, movimento)
        })
        detalheMovimentoScreen(
            onBackNavigationClick = {
                navController.popBackStack()
            }
        )
    }
}

fun NavController.navigateToBottomAppBarItem(
    item: BottomAppBarItem,
) {
    when (item) {
        BottomAppBarItem.Programacao -> {
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

        else -> {}
    }
}