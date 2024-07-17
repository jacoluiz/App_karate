package br.com.shubudo.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KarateTopAppBar(
    showBottomBack: Boolean = false,
    onBackNavigationClick: () -> Unit = {},
    texto: String,
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            if (showBottomBack) {
                IconButton(onClick = { onBackNavigationClick() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }
        },
        title = {
            Text(
                text = texto,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF8A2BE2)
        )
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