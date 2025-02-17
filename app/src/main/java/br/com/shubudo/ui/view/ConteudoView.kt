package br.com.shubudo.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlusOne
import androidx.compose.material.icons.outlined.SportsMartialArts
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.R
import br.com.shubudo.SessionManager
import br.com.shubudo.ui.components.CustomIconButton
import br.com.shubudo.ui.components.DropDownMenuCard
import br.com.shubudo.ui.components.LoadingOverlay
import br.com.shubudo.ui.theme.LightPrimaryContainerColorAmarela
import br.com.shubudo.ui.theme.PrimaryColorRoxa
import br.com.shubudo.ui.uistate.ProgramacaoUiState
import br.com.shubudo.ui.viewModel.ThemeViewModel

@Composable
fun ProgramacaoView(
    uiState: ProgramacaoUiState,
    onClickFaixa: (String) -> Unit,
    themeViewModel: ThemeViewModel
) {
    when (uiState) {
        is ProgramacaoUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Cabeçalho colorido (apenas para exibir o Loading)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
                LoadingOverlay(true) {}
            }
        }
        is ProgramacaoUiState.Empty -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Calma jovem, ainda não temos nada por aqui!",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
        is ProgramacaoUiState.Success -> {
            Column(modifier = Modifier.fillMaxSize()) {
                // Cabeçalho com fundo colorido apenas atrás do texto
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(vertical = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Explore todo o conteúdo de cada faixa e aproveite dicas e materiais extras que preparamos para você!",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Conteúdo principal
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 28.dp)
                ) {
                    // Surface para conter os menus (com fundo branco ou definido pelo tema)

                        Column(modifier = Modifier.padding(16.dp)) {
                            // Primeiro drop-down para Programação
                            DropDownMenuCard(
                                titulo = "Programação",
                                icone = Icons.Outlined.SportsMartialArts
                            ) {
                                Column {
                                    uiState.faixas.forEach { faixa ->
                                        if (!listaBloqueiFaixas().contains(faixa.faixa)) {
                                            val iconPainter = if (faixa.faixa == "Branca" && !isSystemInDarkTheme()) {
                                                painterResource(id = R.drawable.ic_faixa_outline)
                                            } else {
                                                painterResource(id = R.drawable.ic_faixa)
                                            }
                                            CustomIconButton(
                                                texto = faixa.faixa,
                                                iconPainter = iconPainter,
                                                onClick = {
                                                    themeViewModel.changeThemeFaixa(faixa.faixa)
                                                    onClickFaixa(faixa.faixa)
                                                },
                                                cor = selecionaCorIcone(
                                                    faixa.faixa,
                                                    isSystemInDarkTheme()
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                            // Segundo drop-down para Conteúdo Adicional (vazio neste exemplo)
                            DropDownMenuCard(
                                titulo = "Conteudo adicional",
                                icone = Icons.Outlined.PlusOne
                            ) {}
                        }

                }
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
