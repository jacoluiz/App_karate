package br.com.shubudo.ui.view

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlusOne
import androidx.compose.material.icons.outlined.SportsMartialArts
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.shubudo.R
import br.com.shubudo.ui.components.CustomIconButton
import br.com.shubudo.ui.components.DropDownMenuCard
import br.com.shubudo.ui.theme.LightPrimaryContainerColorAmarela
import br.com.shubudo.ui.theme.PrimaryColorRoxa
import br.com.shubudo.ui.uistate.ProgramacaoUiState

@Composable
fun ProgramacaoView(
    changeThemeFaixa: (String) -> Unit,
    uiState: ProgramacaoUiState, onClickFaixa: (String) -> Unit
) {
    when (uiState) {
        is ProgramacaoUiState.Success -> {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                DropDownMenuCard(titulo = "Programação", icone = Icons.Outlined.SportsMartialArts) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        modifier = Modifier.padding(1.dp)
                    ) {
                        uiState.faixas.forEach { faixa ->
                            if (listaBloqueiFaixas().contains(faixa.faixa).not()) {
                                val iconPainter =
                                    if (faixa.faixa == "Branca" && !isSystemInDarkTheme()) {
                                        painterResource(id = R.drawable.ic_faixa_outline)
                                    } else {
                                        painterResource(id = R.drawable.ic_faixa)
                                    }
                                CustomIconButton(
                                    texto = faixa.faixa,
                                    iconPainter = iconPainter,
                                    onClick = {
                                        onClickFaixa(faixa.faixa)
                                        changeThemeFaixa(faixa.faixa)
                                    },
                                    cor = selecionaCorIcone(faixa.faixa, isSystemInDarkTheme())
                                )
                            }
                        }
                    }
                }
                DropDownMenuCard(titulo = "Conteudo adicional", icone = Icons.Outlined.PlusOne) {}
            }
        }

        is ProgramacaoUiState.Empty -> {
            Box(Modifier.fillMaxSize()) {
                Text(
                    text = "Calma jovem, ainda não temos nada por aqui!",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }

        is ProgramacaoUiState.Loading -> {
            Box(Modifier.fillMaxSize()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
                ) {}
                CircularProgressIndicator(
                    Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

fun selecionaCorIcone(faixa: String, isDarkTheme: Boolean): Color {
    return when (faixa) {
        "Branca" -> if (isDarkTheme) Color.White else Color.Black
        "Amarela" -> LightPrimaryContainerColorAmarela
        "Laranja" -> Color(0xFFF46000)
        "Verde" -> Color(0xFF00800D)
        "Roxa" -> PrimaryColorRoxa
        "Marrom" -> Color(0xFF6E3812)
        "Preta" -> Color.Black
        "Mestre" -> Color(0xFF8A2BE2)
        "Grão Mestre" -> Color(0xFF8A2BE2)
        else -> Color(0xFF8A2BE2)
    }
}

fun listaBloqueiFaixas(): List<String> {
    return listOf("Preta", "Mestre", "Grão Mestre")
}

@Composable
@Preview
fun ProgramacaoContentPreview() {

}