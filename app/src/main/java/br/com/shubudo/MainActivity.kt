package br.com.shubudo

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.shubudo.navigation.*
import br.com.shubudo.ui.components.appBar.BottomAppBarItem
import br.com.shubudo.ui.components.appBar.KarateBottomAppBar
import br.com.shubudo.ui.components.appBar.KarateTopAppBar
import br.com.shubudo.ui.theme.AppShubudoTheme
import br.com.shubudo.ui.viewModel.DropDownMenuViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val REQUEST_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        hideSystemUI()
        super.onCreate(savedInstanceState)
        checkAndRequestPermissions()

        setContent {
            val themeViewModel: ThemeViewModel = viewModel() // Injeção do ViewModel
            val dropDownMenuViewModel: DropDownMenuViewModel = viewModel() // Injeção do ViewModel

            AppShubudoTheme(faixa = themeViewModel.currentFaixa.value) {
                val navController = rememberNavController()
                val backStackEntryState by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntryState?.destination
                Surface {
                    // Implementação da UI, conforme o código original...
                    val isShowTopBar = when (currentDestination?.route) {
                        detalheMovimentoRuteFullpath -> false
                        AppDestination.Login.route -> false
                        else -> true
                    }

                    val isShowBottomBar = when (currentDestination?.route) {
                        AppDestination.Avisos.route, AppDestination.Perfil.route, AppDestination.Programacao.route -> true
                        else -> false
                    }

                    // Definições dos títulos, botões, etc., conforme o código original
                    val topAppBarTitle = when (currentDestination?.route) {
                        editarPerfilRoute -> "Editar Perfil"
                        perfilRoute -> "Perfil"
                        AppDestination.Avisos.route -> "Seja Bem-Vindo"
                        AppDestination.Login.route -> "Login"
                        AppDestination.Programacao.route -> "Conteúdo"
                        detalheFaixaRuteFullpath -> ("Faixa " + backStackEntryState?.arguments?.getString(detalheFaixaArgument))
                        detalheMovimentoRuteFullpath -> backStackEntryState?.arguments?.getString(detalheMovimentoArgument)
                        novoUsuarioRote -> "Precisamos de alguns dados"
                        novoUsuarioRoteSemUsername -> "Precisamos de alguns dados"
                        else -> "Shubu-dô App"
                    }

                    val showBottomBack = when (currentDestination?.route) {
                        detalheFaixaRuteFullpath, novoUsuarioRote, novoUsuarioRoteSemUsername, esqueciMinhaSenhaRote, esqueciMinhaSenhaRoteSemUsername -> true
                        else -> false
                    }

                    val showColorTopAppBar = when (currentDestination?.route) {
                        detalheMovimentoRuteFullpath, AppDestination.Login.route -> false
                        else -> true
                    }

                    val showTitleTopAppBar = when (currentDestination?.route) {
                        detalheMovimentoRuteFullpath, AppDestination.Login.route -> false
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

    private fun checkAndRequestPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val permissions = arrayOf(
                android.Manifest.permission.READ_MEDIA_IMAGES,
                android.Manifest.permission.READ_MEDIA_VIDEO,
                android.Manifest.permission.READ_MEDIA_AUDIO
            )
            requestPermissionsIfNecessary(permissions)
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val permissions = arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            requestPermissionsIfNecessary(permissions)
        }
    }

    private fun requestPermissionsIfNecessary(permissions: Array<String>) {
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), REQUEST_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permissões concedidas
                // Log.i("Permission", "Todas as permissões foram concedidas.")
            } else {
                // Permissões negadas
                // Log.e("Permission", "Algumas permissões foram negadas.")
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
                ) {

                }
                content()
            }
        }
    }
}
