package br.com.shubudo.ui.components.appBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.shubudo.navigation.AppDestination
import br.com.shubudo.navigation.perfilRoute
import br.com.shubudo.navigation.eventosRoute
import br.com.shubudo.navigation.programacaoRoute

sealed class BottomAppBarItem(
    val icon: ImageVector, val label: String, val route: String
) {
    object Conteudo : BottomAppBarItem(
        Icons.Default.SportsMartialArts, "Conteudo", programacaoRoute
    )

    object Perfil : BottomAppBarItem(
        Icons.Default.AccountCircle, "Perfil", perfilRoute
    )

    object Eventos : BottomAppBarItem(
        Icons.Default.Event, "Eventos", eventosRoute
    )
}

@Composable
fun KarateBottomAppBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomAppBarItem) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        BottomAppBarItem.Eventos,
        BottomAppBarItem.Perfil,
        BottomAppBarItem.Conteudo
    )

    val selectedItem = when {
        currentRoute == AppDestination.Login.route -> BottomAppBarItem.Perfil // força seleção de "Perfil" para a tela de login
        else -> items.firstOrNull { currentRoute?.startsWith(it.route) == true } ?: BottomAppBarItem.Eventos
    }


    BottomAppBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        items.forEach { item ->
            val isSelected = item == selectedItem
            val iconSize by animateDpAsState(
                targetValue = if (isSelected) 42.dp else 28.dp,
                animationSpec = tween(durationMillis = 300), label = ""
            )

            val offset by animateDpAsState(
                targetValue = if (isSelected) (-6).dp else 0.dp,
                animationSpec = tween(durationMillis = 300), label = ""
            )

            val textColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                animationSpec = tween(durationMillis = 300), label = ""
            )

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = null,
                        tint = textColor,
                        modifier = Modifier
                            .size(iconSize)
                            .offset(y = offset)
                    )
                },
                label = {
                    AnimatedVisibility(visible = !isSelected) {
                        Text(
                            text = item.label,
                            color = textColor,
                            fontSize = 12.sp,
                            modifier = Modifier.offset(y = offset)
                        )
                    }
                }
            )
        }
    }
}
