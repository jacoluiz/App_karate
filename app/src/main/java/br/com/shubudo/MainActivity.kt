package br.com.shubudo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.shubudo.navigation.AppDestination
import br.com.shubudo.navigation.KarateNavHost
import br.com.shubudo.navigation.avisosRoute
import br.com.shubudo.navigation.detalheFaixaArgument
import br.com.shubudo.navigation.detalheFaixaRuteFullpath
import br.com.shubudo.navigation.navigateToBottomAppBarItem
import br.com.shubudo.navigation.perfilRoute
import br.com.shubudo.navigation.programacaoRoute
import br.com.shubudo.ui.components.BottomAppBarItem
import br.com.shubudo.ui.components.KarateBottomAppBar
import br.com.shubudo.ui.components.KarateTopAppBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val backStackEntryState by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntryState?.destination
            Surface {
                val isShowTopBar = when (currentDestination?.route) {
                    AppDestination.Avisos.route,
                    AppDestination.Perfil.route,
                    AppDestination.Programacao.route,
                    detalheFaixaRuteFullpath
                    -> true

                    else -> false
                }
                val isShowBottomBar = when (currentDestination?.route) {
                    AppDestination.Avisos.route,
                    AppDestination.Perfil.route,
                    AppDestination.Programacao.route -> true

                    else -> false
                }
                val topAppBarTitle = when (currentDestination?.route) {
                    AppDestination.Avisos.route -> "Bem-Vindo"
                    detalheFaixaRuteFullpath -> ("Faixa " + backStackEntryState?.arguments?.getString(
                        detalheFaixaArgument
                    ))

                    else -> "Shubudo"
                }

                val showBottomBack = when (currentDestination?.route) {
                    detalheFaixaRuteFullpath -> true
                    else -> false
                }

                KarateApp(
                    isShowTopBar = isShowTopBar,
                    isShowBottomBar = isShowBottomBar,
                    topAppBarTitle = topAppBarTitle,
                    showBottomBack = showBottomBack,
                    navController = navController

                ) {
                    KarateNavHost(
                        navController = navController,
                    )
                }
            }

        }
    }
}

@Composable
fun KarateApp(
    isShowTopBar: Boolean = false,
    isShowBottomBar: Boolean = false,
    topAppBarTitle: String,
    showBottomBack: Boolean = false,
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentRoute = currentDestination?.route
    Scaffold(
        topBar = {
            if (isShowTopBar) {
                KarateTopAppBar(
                    texto = topAppBarTitle,
                    showBottomBack = showBottomBack,
                    onBackNavigationClick = {
                        navController.popBackStack()
                    })
            }
        },
        bottomBar = {
            if (isShowBottomBar) {
                KarateBottomAppBar(
                    selectedItem = when (currentRoute) {
                        perfilRoute -> BottomAppBarItem.Perfil
                        avisosRoute -> BottomAppBarItem.Avisos
                        programacaoRoute -> BottomAppBarItem.Programacao
                        else -> BottomAppBarItem.Avisos
                    },
                    items = remember {
                        listOf(
                            BottomAppBarItem.Perfil,
                            BottomAppBarItem.Avisos,
                            BottomAppBarItem.Programacao,
                        )
                    },
                    onItemClick =
                    { item ->
                        navController.navigateToBottomAppBarItem(item)
                    }
                )
            }
        }
    )
    {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                color = Color(0xFF8A2BE2),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            ) {}
            content()
        }
    }
}
