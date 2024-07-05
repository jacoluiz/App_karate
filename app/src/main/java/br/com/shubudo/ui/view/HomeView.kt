package br.com.shubudo.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.SportsMartialArts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.shubudo.ui.components.CustomIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val navController = rememberNavController()

    Scaffold(topBar = {
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
    }, content = { paddingValues ->
        NavHostContainer(
            navController = navController,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()

        )
    }, bottomBar = {
        BottomAppBar {
            CustomIconButton(
                icon = Icons.Filled.AccountCircle,
                text = "Perfil",
                selected = navController.currentDestination?.route == "perfil",
                onClick = { navController.navigate("perfil") },
                modifier = Modifier.weight(1f)
            )
            CustomIconButton(
                icon = Icons.Outlined.Event,
                text = "Avisos",
                selected = navController.currentDestination?.route == "home",
                onClick = { navController.navigate("home") },
                modifier = Modifier.weight(1f)
            )
            CustomIconButton(
                icon = Icons.Outlined.List,
                text = "Conteudo",
                selected = navController.currentDestination?.route == "programacao",
                onClick = { navController.navigate("programacao") },
                modifier = Modifier.weight(1f)
            )
        }
    })
}

@Composable
fun NavHostContainer(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = "avisos", modifier = modifier) {
        composable("avisos") { AvisosContent() }
        composable("perfil") { PerfilContent() }
        composable("programacao") { ProgramacaoContent() }
    }
}
