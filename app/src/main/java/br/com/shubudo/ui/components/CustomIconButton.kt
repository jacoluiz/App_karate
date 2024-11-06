package br.com.shubudo.ui.components

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomIconButton(
    icon: ImageVector? = null,
    iconPainter: Painter? = null,
    texto: String,
    cor : Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    require(icon != null || iconPainter != null) { "Either 'icon' or 'iconPainter' must be provided." }

    IconButton(
        enabled = enabled,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 0.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = texto,
                    tint =  cor,
                    modifier = Modifier.size(28.dp),
                )

            } else if (iconPainter != null) {
                Icon(
                    painter = iconPainter,
                    contentDescription = texto,
                    tint = cor,
                    modifier = Modifier.size(28.dp),
                )
            }

            Text(
                text = texto,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.size(28.dp))
        }
    }
}
