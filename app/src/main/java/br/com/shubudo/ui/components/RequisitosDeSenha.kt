package br.com.shubudo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RequisitosDeSenha(text: String, isMet: Boolean, bringIntoViewRequester: Any) {
    AnimatedVisibility(
        visible = !isMet,
        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -50 }) // Animação ao desaparecer
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Requisito não atendido",
                tint = Color.Red
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                color = Color.Red,
                modifier = Modifier.bringIntoViewRequester(bringIntoViewRequester as BringIntoViewRequester)
            )
        }
    }
}