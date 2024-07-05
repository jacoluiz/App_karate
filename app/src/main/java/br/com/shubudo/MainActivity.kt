package br.com.shubudo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.shubudo.navigation.AppDestination
import br.com.shubudo.ui.components.CustomIconButton
import br.com.shubudo.ui.view.AvisosContent
import br.com.shubudo.ui.view.PerfilContent
import br.com.shubudo.ui.view.ProgramacaoContent


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
                    AppDestination.Programacao.route -> true

                    else -> false
                }
                val isShowBottomBar = when (currentDestination?.route) {
                    AppDestination.Avisos.route,
                    AppDestination.Perfil.route,
                    AppDestination.Programacao.route -> true

                    else -> false
                }
                karateApp(
                    isShowTopBar = isShowTopBar,
                    isShowBottomBar = isShowBottomBar,
                    navController = navController
                ) {
                    NavHostContainer(
                        navController = navController,
                        modifier = Modifier
                            .fillMaxWidth()

                    )
                }
            }

        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun karateApp(
    isShowTopBar: Boolean = false,
    isShowBottomBar: Boolean = false,
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            if (isShowTopBar) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Shubudo",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }, colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF8A2BE2)
                    )
                )
            }
        },
        bottomBar = {
            if (isShowBottomBar) {
                BottomAppBar {
                    CustomIconButton(
                        icon = Icons.Filled.AccountCircle,
                        text = "Perfil",
                        selected = navController.currentDestination?.route == AppDestination.Perfil.route,
                        onClick = {
                            navController.navigate(AppDestination.Perfil.route) {
                                launchSingleTop = true
                                popUpTo(AppDestination.Perfil.route)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    CustomIconButton(
                        icon = Icons.Outlined.Event,
                        text = "Avisos",
                        selected = navController.currentDestination?.route == AppDestination.Avisos.route,
                        onClick = {
                            navController.navigate(AppDestination.Avisos.route) {
                                launchSingleTop = true
                                popUpTo(AppDestination.Avisos.route)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    CustomIconButton(
                        icon = Icons.Outlined.List,
                        text = "Conteudo",
                        selected = navController.currentDestination?.route == AppDestination.Programacao.route,
                        onClick = {
                            navController.navigate(AppDestination.Programacao.route) {
                                launchSingleTop = true
                                popUpTo(AppDestination.Programacao.route)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    )
    {
        Box(
            modifier = Modifier.padding(it)
        ) { content() }
    }
}

@Composable
fun NavHostContainer(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = "avisos", modifier = modifier) {
        composable(AppDestination.Avisos.route) { AvisosContent() }
        composable(AppDestination.Perfil.route) { PerfilContent() }
        composable(AppDestination.Programacao.route) { ProgramacaoContent() }
    }
}
