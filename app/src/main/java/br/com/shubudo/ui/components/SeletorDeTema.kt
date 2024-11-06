package br.com.shubudo.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.shubudo.ui.theme.LightPrimaryColorAmarela
import br.com.shubudo.ui.theme.LightPrimaryColorBranca
import br.com.shubudo.ui.theme.LightPrimaryColorLaranja
import br.com.shubudo.ui.theme.LightPrimaryColorMarron
import br.com.shubudo.ui.theme.LightPrimaryColorRoxa
import br.com.shubudo.ui.theme.LightPrimaryColorVerde
import br.com.shubudo.ui.theme.PrimaryColorAmarela
import br.com.shubudo.ui.theme.PrimaryColorBranca
import br.com.shubudo.ui.theme.PrimaryColorLaranja
import br.com.shubudo.ui.theme.PrimaryColorMarron
import br.com.shubudo.ui.theme.PrimaryColorRoxa
import br.com.shubudo.ui.theme.PrimaryColorVerde
import br.com.shubudo.ui.viewModel.ThemeViewModel

@Composable
fun SeletorDeTema(
    themeViewModel: ThemeViewModel
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            16.dp,
            Alignment.CenterHorizontally
        )
    ) {
        val colors = mapOf(
            "Branca" to if (themeViewModel.eTemaEscuro()) PrimaryColorBranca else LightPrimaryColorBranca,
            "Amarela" to if (themeViewModel.eTemaEscuro()) PrimaryColorAmarela else LightPrimaryColorAmarela,
            "Laranja" to if (themeViewModel.eTemaEscuro()) PrimaryColorLaranja else LightPrimaryColorLaranja,
            "Verde" to if (themeViewModel.eTemaEscuro()) PrimaryColorVerde else LightPrimaryColorVerde,
            "Roxa" to if (themeViewModel.eTemaEscuro()) PrimaryColorRoxa else LightPrimaryColorRoxa,
            "Marrom" to if (themeViewModel.eTemaEscuro()) PrimaryColorMarron else LightPrimaryColorMarron
        )

        colors.forEach { (faixa, cor) ->
            Button(
                border = null,
                onClick = { themeViewModel.changeThemeFaixa(faixa) },
                modifier = Modifier
                    .size(32.dp), // Tamanho do botão para criar um círculo
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = cor, // Cor do fundo conforme a faixa
                    contentColor = Color.White
                )
            ) {}
        }
    }
}