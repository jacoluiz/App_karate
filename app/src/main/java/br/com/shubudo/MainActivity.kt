package br.com.shubudo

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
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
import br.com.shubudo.navigation.AppDestination
import br.com.shubudo.navigation.KarateNavHost
import br.com.shubudo.navigation.avisosRoute
import br.com.shubudo.navigation.detalheFaixaArgument
import br.com.shubudo.navigation.detalheFaixaRuteFullpath
import br.com.shubudo.navigation.detalheMovimentoArgument
import br.com.shubudo.navigation.detalheMovimentoRuteFullpath
import br.com.shubudo.navigation.editarPerfilRoute
import br.com.shubudo.navigation.esqueciMinhaSenhaRote
import br.com.shubudo.navigation.esqueciMinhaSenhaRoteSemUsername
import br.com.shubudo.navigation.eventosRoute
import br.com.shubudo.navigation.novoUsuarioRote
import br.com.shubudo.navigation.novoUsuarioRoteSemUsername
import br.com.shubudo.navigation.perfilRoute
import br.com.shubudo.navigation.programacaoRoute
import br.com.shubudo.navigation.recursosRoute
import br.com.shubudo.ui.components.appBar.BottomAppBarItem
import br.com.shubudo.ui.components.appBar.KarateBottomAppBar
import br.com.shubudo.ui.components.appBar.KarateTopAppBar
import br.com.shubudo.ui.theme.AppShubudoTheme
import br.com.shubudo.ui.view.OfflineScreen
import br.com.shubudo.ui.viewModel.DropDownMenuViewModel
import br.com.shubudo.ui.viewModel.PerfilViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel
import br.com.shubudo.utils.isInternetAvailable
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val REQUEST_PERMISSION_CODE = 100
    private val REQUEST_CODE_UPDATE = 1234
    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        hideSystemUI()
        super.onCreate(savedInstanceState)
        appUpdateManager = AppUpdateManagerFactory.create(this)
        FirebaseApp.initializeApp(this)
        checkAndRequestPermissions()
        SessionManager.inicializar(applicationContext)

        setContent {
            val context = LocalContext.current
            var isOnline by remember { mutableStateOf<Boolean?>(null) }

            LaunchedEffect(Unit) {
                isOnline = isInternetAvailable(context)
            }

            when (isOnline) {
                null -> {}
                false -> OfflineScreen(onRetry = { isOnline = isInternetAvailable(context) })
                true -> {
                    val themeViewModel: ThemeViewModel = viewModel()
                    val dropDownMenuViewModel: DropDownMenuViewModel = viewModel()
                    val perfilViewModel: PerfilViewModel = viewModel()
                    val uiState by perfilViewModel.uiState.collectAsState()
                    val isLoggedIn = uiState is br.com.shubudo.ui.uistate.PerfilUiState.Success

                    val faixaParaTema = themeViewModel.getCurrentFaixa()
                        ?: SessionManager.usuarioLogado?.corFaixa
                        ?: themeViewModel.getFaixaAtualOuAleatoria()

                    AppShubudoTheme(faixa = faixaParaTema) {
                        val navController = rememberNavController()
                        val backStackEntryState by navController.currentBackStackEntryAsState()
                        val currentDestination = backStackEntryState?.destination

                        Surface {
                            val isShowTopBar = when (currentDestination?.route) {
                                detalheMovimentoRuteFullpath, AppDestination.Login.route -> false
                                else -> true
                            }

                            val isShowBottomBar = when (currentDestination?.route) {
                                AppDestination.Recursos.route,
                                AppDestination.Perfil.route,
                                AppDestination.Login.route -> true
                                else -> false
                            }

                            val topAppBarTitle = when (currentDestination?.route) {
                                editarPerfilRoute -> "Editar Perfil"
                                perfilRoute -> "Perfil"
                                eventosRoute -> "Evento"
                                AppDestination.Login.route -> "Login"
                                programacaoRoute -> "Conteúdo"
                                detalheFaixaRuteFullpath -> "Faixa " + backStackEntryState?.arguments?.getString(detalheFaixaArgument)
                                detalheMovimentoRuteFullpath -> backStackEntryState?.arguments?.getString(detalheMovimentoArgument)
                                novoUsuarioRote, novoUsuarioRoteSemUsername -> "Precisamos de alguns dados"
                                else -> "Shubu-dô App"
                            }

                            val showBottomBack = when (currentDestination?.route) {
                                detalheFaixaRuteFullpath,
                                novoUsuarioRote,
                                novoUsuarioRoteSemUsername,
                                esqueciMinhaSenhaRote,
                                avisosRoute,
                                eventosRoute,
                                programacaoRoute,
                                esqueciMinhaSenhaRoteSemUsername -> true
                                else -> false
                            }

                            val showColorTopAppBar = currentDestination?.route != detalheMovimentoRuteFullpath && currentDestination?.route != AppDestination.Login.route
                            val showTitleTopAppBar = showColorTopAppBar

                            KarateApp(
                                isShowTopBar = isShowTopBar,
                                isShowBottomBar = isShowBottomBar,
                                topAppBarTitle = topAppBarTitle ?: "",
                                showBottomBack = showBottomBack,
                                navController = navController,
                                showColorTopAppBar = showColorTopAppBar,
                                showTitleTopAppBar = showTitleTopAppBar,
                                themeViewModel = themeViewModel,
                                isLoggedIn = isLoggedIn
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
    }

    override fun onResume() {
        super.onResume()
        checkForUpdate()
    }

    private fun checkForUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    REQUEST_CODE_UPDATE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPDATE && resultCode != Activity.RESULT_OK) {
            finish()
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
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSION_CODE
            )
        }
    }

    private fun hideSystemUI() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.let {
            it.hide(WindowInsetsCompat.Type.navigationBars())
            it.show(WindowInsetsCompat.Type.statusBars())
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
    navController: NavHostController,
    showColorTopAppBar: Boolean = true,
    showTitleTopAppBar: Boolean = true,
    themeViewModel: ThemeViewModel,
    isLoggedIn: Boolean,
    content: @Composable () -> Unit
) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.statusBarsPadding(),
            topBar = {
                if (isShowTopBar) {
                    KarateTopAppBar(
                        texto = topAppBarTitle,
                        showBottomBack = showBottomBack,
                        showColor = showColorTopAppBar,
                        showTitle = showTitleTopAppBar,
                        onBackNavigationClick = {
                            navController.popBackStack()
                        }
                    )
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
                    KarateBottomAppBar(
                        navController = navController,
                        onItemClick = { item ->
                            when (item) {
                                BottomAppBarItem.Perfil -> {
                                    if (isLoggedIn) {
                                        navController.navigate(perfilRoute) {
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    } else {
                                        navController.navigate(AppDestination.Login.route) {
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }

                                BottomAppBarItem.Recursos -> {
                                    navController.navigate(recursosRoute) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        }
                    )

                }
            }
        ) {
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
                content()
            }
        }
    }
}
