package br.com.shubudo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import br.com.shubudo.ui.components.BottomAppBarItem

@Composable
fun KarateNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController, startDestination = avisosRoute) {
        programacaoScreen(onNavigateToDetalheFaixa = {
            navController.navigateToDetalheFaixa(
                it
            )
        })
        avisosScreen()
        perfilScreen()
        detalheFaixaScreen()
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
    }
}