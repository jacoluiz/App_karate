package br.com.shubudo.ui.components.appBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.room.util.copy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KarateTopAppBar(
    showBottomBack: Boolean = false,
    onBackNavigationClick: () -> Unit = {},
    showColor: Boolean = true,
    showTitle: Boolean = true,
    texto: String,
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            if (showBottomBack) {
                IconButton(onClick = { onBackNavigationClick() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Seta para voltar",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        },
        title = {
            Text(
                text = if (showTitle) texto else "",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (showColor) MaterialTheme.colorScheme.primary else Color.Transparent,
        ),
        modifier = if (showColor) Modifier else Modifier
            .zIndex(1f)
            .background(Color.Transparent)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomTopAppBar() {
    KarateTopAppBar(
        texto = "My App",
        showBottomBack = true
    )
}