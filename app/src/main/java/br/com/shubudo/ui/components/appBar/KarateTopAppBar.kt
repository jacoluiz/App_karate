package br.com.shubudo.ui.components.appBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun KarateTopAppBar(
    showBottomBack: Boolean = false,
    onBackNavigationClick: () -> Unit = {},
    showColor: Boolean = true,
    showTitle: Boolean = true,
    texto: String,
) {
    val backgroundColor = if (showColor) MaterialTheme.colorScheme.primary else Color.Transparent
    val contentColor = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .zIndex(1f)
            .height(56.dp) // Altura padrão do TopAppBar
    ) {
        // Ícone de voltar alinhado à esquerda
        if (showBottomBack) {
            IconButton(
                onClick = { onBackNavigationClick() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Seta para voltar",
                    tint = contentColor
                )
            }
        }

        // Título centralizado na tela
        if (showTitle) {
            Text(
                text = texto,
                color = contentColor,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
    }
}
