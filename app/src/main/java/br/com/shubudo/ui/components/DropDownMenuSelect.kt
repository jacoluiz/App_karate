package br.com.shubudo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.ui.viewModel.DropDownMenuViewModel

@Composable
fun DropDownMenuSelect(
    titulo: String,
    icone: ImageVector? = null,
    iconePainter: Painter? = null,
    colorIcone: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    tamanhoIcone: Int = 24,
    viewmodel: DropDownMenuViewModel,
    conteudo: @Composable () -> Unit
) {
    val expanded by viewmodel.expanded
    val selected by viewmodel.selected

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300), label = "Animação de rotação do icone"
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier
            .animateContentSize(),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        onClick = { viewmodel.changeExpanded(!expanded) },

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                .background(MaterialTheme.colorScheme.tertiary)
                .height(56.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically

        ) {
            if (icone != null) {
                Icon(
                    imageVector = icone,
                    contentDescription = "Icon",
                    modifier = Modifier.size(tamanhoIcone.dp),
                    tint = colorIcone
                )
            } else if (iconePainter != null) {
                Icon(
                    painter = iconePainter,
                    contentDescription = "Icon",
                    modifier = Modifier.size(tamanhoIcone.dp),
                    tint = colorIcone
                )
            }

            Text(
                if (!selected) "Selecione sua faixa" else "Selecionado ${viewmodel.faixaSelecionada.value}",
                modifier = Modifier.padding(start = 16.dp),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onPrimary

            )

            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Dropdown Icon",
                modifier = Modifier.rotate(rotation),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            conteudo()
        }
    }
}