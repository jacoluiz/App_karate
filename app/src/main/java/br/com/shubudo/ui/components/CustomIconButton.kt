package br.com.shubudo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomIconButton(
    icon: ImageVector,
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shadowSize = if (selected) 6.dp else 0.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.shadow(shadowSize, RoundedCornerShape(12.dp))
    ) {
        IconButton(onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = Color(0xFF8A2BE2),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = text,
                    color = Color(0xFF8A2BE2),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}