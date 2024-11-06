package br.com.shubudo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.shubudo.navigation.AppDestination
import br.com.shubudo.navigation.KarateNavHost
import br.com.shubudo.navigation.avisosRoute
import br.com.shubudo.navigation.detalheFaixaArgument
import br.com.shubudo.navigation.detalheFaixaRuteFullpath
import br.com.shubudo.navigation.detalheMovimentoArgument
import br.com.shubudo.navigation.detalheMovimentoRuteFullpath
import br.com.shubudo.navigation.esqueciMinhaSenhaRote
import br.com.shubudo.navigation.esqueciMinhaSenhaRoteSemUsername
import br.com.shubudo.navigation.navigateToBottomAppBarItem
import br.com.shubudo.navigation.novoUsuarioRote
import br.com.shubudo.navigation.novoUsuarioRoteSemUsername
import br.com.shubudo.navigation.perfilRoute
import br.com.shubudo.navigation.programacaoRoute
import br.com.shubudo.ui.components.appBar.BottomAppBarItem
import br.com.shubudo.ui.components.appBar.KarateBottomAppBar
import br.com.shubudo.ui.components.appBar.KarateTopAppBar
import br.com.shubudo.ui.theme.AppShubudoTheme
import br.com.shubudo.ui.view.LoginView
import br.com.shubudo.ui.viewModel.DropDownMenuViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        hideSystemUI()
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel() // Injeção do ViewModel
            val dropDownMenuViewModel: DropDownMenuViewModel = viewModel() // Injeção do ViewModel

            AppShubudoTheme(faixa = themeViewModel.currentFaixa.value) {
                val navController = rememberNavController()
                val backStackEntryState by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntryState?.destination
                Surface {
                    val isShowTopBar = when (currentDestination?.route) {
                        detalheMovimentoRuteFullpath -> false
                        AppDestination.Login.route -> false
                        else -> true
                    }

                    val isShowBottomBar = when (currentDestination?.route) {
                        AppDestination.Avisos.route, AppDestination.Perfil.route, AppDestination.Programacao.route -> true
                        else -> false
                    }

                    val topAppBarTitle = when (currentDestination?.route) {
                        AppDestination.Avisos.route -> "Bem-Vindo"
                        AppDestination.Login.route -> "Login"
                        AppDestination.Programacao.route -> "Conteúdo"
                        detalheFaixaRuteFullpath -> ("Faixa " + backStackEntryState?.arguments?.getString(
                            detalheFaixaArgument
                        ))

                        detalheMovimentoRuteFullpath -> backStackEntryState?.arguments?.getString(
                            detalheMovimentoArgument
                        )

                        novoUsuarioRote -> "Precisamos de alguns dados"
                        novoUsuarioRoteSemUsername -> "Precisamos de alguns dados"


                        else -> "Shubu-dô App"
                    }

                    val showBottomBack = when (currentDestination?.route) {
                        detalheFaixaRuteFullpath -> true
                        novoUsuarioRote -> true
                        novoUsuarioRoteSemUsername -> true
                        esqueciMinhaSenhaRote -> true
                        esqueciMinhaSenhaRoteSemUsername -> true

                        else -> false
                    }

                    val showColorTopAppBar = when (currentDestination?.route) {
                        detalheMovimentoRuteFullpath -> false
                        AppDestination.Login.route -> false
                        else -> true
                    }

                    val showTitleTopAppBar = when (currentDestination?.route) {
                        detalheMovimentoRuteFullpath -> false
                        AppDestination.Login.route -> false
                        else -> true
                    }

                    if (topAppBarTitle != null) {
                        KarateApp(
                            isShowTopBar = isShowTopBar,
                            isShowBottomBar = isShowBottomBar,
                            topAppBarTitle = topAppBarTitle,
                            showBottomBack = showBottomBack,
                            navController = navController,
                            showColorTopAppBar = showColorTopAppBar,
                            showTitleTopAppBar = showTitleTopAppBar,
                            themeViewModel = themeViewModel
                        ) {
                            KarateNavHost(
                                navController = navController,
                                themeViewModel = themeViewModel,
                                dropDownMenuViewModel = dropDownMenuViewModel

                            )
                        }
                    }
                }
            }
        }
    }

    private fun hideSystemUI() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController?.let {
            // Ocultar a barra de status e a barra de navegação
            it.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
            // Modo imersivo persistente
            it.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}

@Composable
fun KarateApp(
    isShowTopBar: Boolean = false,
    isShowBottomBar: Boolean = false,
    topAppBarTitle: String,
    testeDeTela: Boolean = false,
    showBottomBack: Boolean = false,
    showColorTopAppBar: Boolean = true,
    navController: NavHostController,
    showTitleTopAppBar: Boolean = true,
    themeViewModel: ThemeViewModel,
    content: @Composable () -> Unit
) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentRoute = currentDestination?.route
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                if (isShowTopBar) {
                    KarateTopAppBar(
                        texto = topAppBarTitle,
                        showBottomBack = showBottomBack,
                        showColor = showColorTopAppBar,
                        showTitle = showTitleTopAppBar,
                        onBackNavigationClick = {
                            navController.popBackStack()
                        })
                }
            },
            floatingActionButton = {
                if (testeDeTela) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .height(56.dp)
                            .width(56.dp)
                            .padding(8.dp)
                            .background(Color(0xFF8A2BE2), RoundedCornerShape(28.dp))
                    )
                }
            },
            bottomBar = {
                if (isShowBottomBar) {
                    KarateBottomAppBar(selectedItem = when (currentRoute) {
                        perfilRoute -> BottomAppBarItem.Perfil
                        avisosRoute -> BottomAppBarItem.Avisos
                        programacaoRoute -> BottomAppBarItem.Conteudo
                        else -> BottomAppBarItem.Avisos
                    }, items = remember {
                        listOf(
                            BottomAppBarItem.Perfil,
                            BottomAppBarItem.Avisos,
                            BottomAppBarItem.Conteudo,
                        )
                    }, onItemClick = { item ->
                        navController.navigateToBottomAppBarItem(item)
                    })
                }
            }) {
            Box(

                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    }
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                ) {}
                content()
            }
        }
    }
}
