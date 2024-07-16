package br.com.shubudo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import br.com.shubudo.ui.theme.unselectedIconBottomAppBarColor
import br.com.shubudo.ui.theme.unselectedTextBottomAppBarColor

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
    ) {
        items.forEach {
            NavigationBarItem(
                selected = it == selectedItem,
                onClick = { onItemClick(it) },
                icon = {
                    Column {
                        Icon(
                            it.icon,
                            contentDescription = null,
                            tint = if (it == selectedItem) MaterialTheme.colorScheme.primary else unselectedIconBottomAppBarColor
                        )
                    }
                },
                label = {
                    Text(text = it.label)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = unselectedIconBottomAppBarColor,
                    unselectedTextColor = unselectedTextBottomAppBarColor
                )
            )
        }
    }
}

@Composable
@Preview
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