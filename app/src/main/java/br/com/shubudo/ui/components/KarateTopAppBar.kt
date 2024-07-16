package br.com.shubudo.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
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
    texto: String,
    navigationIcon: ImageVector? = null,
    navigationIconClick: (() -> Unit)? = null,
) {
    TopAppBar(
        navigationIcon = {
            if (navigationIconClick != null) {
                IconButton(onClick = { navigationIconClick?.invoke() }) {
                    if (navigationIcon != null) {
                        Icon(imageVector = navigationIcon, contentDescription = null)
                    }
                }
            } else {
                null
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
        navigationIcon = Icons.Default.ArrowBack,
        navigationIconClick = { /* Do something */ }
    )
}