package br.com.shubudo.ui.components.appBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Message
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.shubudo.ui.theme.unselectedIconBottomAppBarColor

sealed class BottomAppBarItem(
    val icon: ImageVector, val label: String
) {
    object Programacao : BottomAppBarItem(
        Icons.Default.SportsMartialArts, "Programacao"
    )

    object Avisos : BottomAppBarItem(
        Icons.Default.Message, "Avisos"
    )

    object Perfil : BottomAppBarItem(
        Icons.Default.AccountCircle, "Perfil"
    )
}

@Composable
fun KarateBottomAppBar(
    selectedItem: BottomAppBarItem,
    items: List<BottomAppBarItem>,
    modifier: Modifier = Modifier,
    onItemClick: (BottomAppBarItem) -> Unit
) {
    BottomAppBar(
        modifier,
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
    ) {
        items.forEach {
            val isSelected = it == selectedItem
            val iconSize by animateDpAsState(
                targetValue = if (isSelected) 42.dp else 28.dp,
                animationSpec = tween(durationMillis = 300)
            )

            val offset by animateDpAsState(
                targetValue = if (isSelected) (-6).dp else 0.dp,
                animationSpec = tween(durationMillis = 300)
            )

            val textColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else unselectedIconBottomAppBarColor,
                animationSpec = tween(durationMillis = 300)
            )

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClick(it) },
                icon = {
                    Icon(
                        it.icon,
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
                            text = it.label,
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

@Composable
@Preview(showBackground = true)
fun KarateBottomAppBarPreview() {
    KarateBottomAppBar(
        selectedItem = BottomAppBarItem.Programacao,
        items = listOf(
            BottomAppBarItem.Programacao,
            BottomAppBarItem.Avisos,
            BottomAppBarItem.Perfil
        )
    ) {}
}