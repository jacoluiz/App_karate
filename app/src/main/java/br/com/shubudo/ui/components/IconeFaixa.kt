package br.com.shubudo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.shubudo.R

@Composable
fun IconeFaixa(
    size: Dp = 22.dp,
    color: Color? = null,
    padding: Dp = 0.dp,
) {
    Image(
        painter = painterResource(id = R.drawable.ic_faixa),
        contentDescription = "Icone de uma faixa",
        modifier = Modifier
            .size(size)
            .padding(padding),
        contentScale = ContentScale.Fit,
        colorFilter = color?.let { ColorFilter.tint(it) }
    )
}

@Composable
@Preview
fun previewIconeFaixa() {
    IconeFaixa()
}