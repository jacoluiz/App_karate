package br.com.shubudo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ItemDetalheMovimento(
    descricao: String,
    valor: String,
    icone: ImageVector? = null,
    iconPainter: Painter? = null,
) {
    val icone = when (valor) {
        "Chute" -> Icons.Default.SportsMartialArts
        "Ataque de mão" -> Icons.Default.FrontHand
        "Defesa" -> Icons.Default.Shield
        else -> icone
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        if (icone != null) {
            Icon(
                imageVector = icone,
                contentDescription = descricao,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary,
            )

        } else if (iconPainter != null) {
            Icon(
                painter = iconPainter,
                contentDescription = descricao,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Text(
            text = "$descricao: $valor",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.inverseSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(4.dp, 0.dp, 24.dp, 0.dp)
        )
    }
}