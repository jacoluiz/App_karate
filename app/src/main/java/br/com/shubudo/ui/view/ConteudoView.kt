package br.com.shubudo.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlusOne
import androidx.compose.material.icons.outlined.SportsMartialArts
import androidx.compose.material3.HorizontalDivider
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
import br.com.shubudo.ui.theme.PrimaryColorMarron
import br.com.shubudo.ui.theme.PrimaryColorPreta
import br.com.shubudo.ui.theme.PrimaryColorMestre
import br.com.shubudo.ui.theme.PrimaryColorGraoMestre
import br.com.shubudo.ui.theme.PrimaryColorRoxa
import br.com.shubudo.ui.uistate.ProgramacaoUiState
import br.com.shubudo.ui.viewModel.ThemeViewModel

// Lista de faixas bloqueadas para usuários que não são avançados (por exemplo, abaixo da preta).
// Se o usuário estiver em uma faixa avançada (cujo valor esteja nesta lista), ele verá todas as opções.
val FAIXAS_BLOQUEADAS = listOf("preta", "mestre", "grão mestre", "preta 1 dan", "preta 2 dan", "preta 3 dan", "preta 4 dan")

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
                // Cabeçalho com fundo colorido atrás do texto
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
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
                // Conteúdo principal com os drop-downs
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 28.dp)
                ) {
                    // Drop-down para Programação
                    DropDownMenuCard(
                        titulo = "Programação",
                        icone = Icons.Outlined.SportsMartialArts
                    ) {
                        // Para possibilitar scroll caso a lista seja longa
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Obtém a faixa do usuário (normalizada)
                            val userFaixa = SessionManager.usuarioLogado?.corFaixa?.trim()?.lowercase() ?: ""
                            uiState.faixas.forEach { faixa ->
                                val faixaNome = faixa.faixa.trim().lowercase()
                                // Se o usuário for avançado (faixa dentro de FAIXAS_BLOQUEADAS), mostra todas as opções.
                                // Caso contrário, mostra somente se a faixa atual não estiver na lista de bloqueadas.
                                if (userFaixa in FAIXAS_BLOQUEADAS || (userFaixa !in FAIXAS_BLOQUEADAS && faixaNome !in FAIXAS_BLOQUEADAS)) {
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
                                        cor = selecionaCorIcone(faixa.faixa, isSystemInDarkTheme())
                                    )
                                }
                            }
                        }
                    }
                    // Drop-down para Conteúdo Adicional (vazio neste exemplo)
                    DropDownMenuCard(
                        titulo = "Conteúdo adicional",
                        icone = Icons.Outlined.PlusOne
                    ) {}
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
        "Marrom" -> PrimaryColorMarron
        "Preta" -> PrimaryColorPreta
        "Preta 1 dan" -> PrimaryColorPreta
        "Preta 2 dan" -> PrimaryColorPreta
        "Preta 3 dan" -> PrimaryColorPreta
        "Preta 4 dan" -> PrimaryColorPreta
        "Mestre" -> PrimaryColorMestre
        "Grão Mestre" -> PrimaryColorGraoMestre
        else -> Color(0xFF8A2BE2)
    }
}
