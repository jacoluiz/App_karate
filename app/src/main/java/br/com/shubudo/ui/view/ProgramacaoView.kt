package br.com.shubudo.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.R
import br.com.shubudo.SessionManager
import br.com.shubudo.ui.components.LoadingWrapper
import br.com.shubudo.ui.theme.LightPrimaryContainerColorAmarela
import br.com.shubudo.ui.theme.PrimaryColorGraoMestre
import br.com.shubudo.ui.theme.PrimaryColorLaranja
import br.com.shubudo.ui.theme.PrimaryColorMarron
import br.com.shubudo.ui.theme.PrimaryColorMestre
import br.com.shubudo.ui.theme.PrimaryColorPreta
import br.com.shubudo.ui.theme.PrimaryColorRoxa
import br.com.shubudo.ui.theme.PrimaryColorVerde
import br.com.shubudo.ui.uistate.ProgramacaoUiState
import br.com.shubudo.ui.viewModel.ThemeViewModel

@Composable
fun ProgramacaoView(
    uiState: ProgramacaoUiState,
    onClickFaixa: (String) -> Unit,
    themeViewModel: ThemeViewModel
) {
    LoadingWrapper(
        isLoading = uiState is ProgramacaoUiState.Loading,
        loadingText = "Carregando conteúdo do karate..."
    ) {
        when (uiState) {
            is ProgramacaoUiState.Loading -> {
                // O loading será mostrado pelo LoadingWrapper
            }

            is ProgramacaoUiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Calma jovem, ainda não temos nada por aqui!",
                        modifier = Modifier.padding(horizontal = 32.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            is ProgramacaoUiState.Success -> {
                LaunchedEffect(Unit) {
                    SessionManager.usuarioLogado?.corFaixa?.let { themeViewModel.changeThemeFaixa(it) }
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    // Cabeçalho com fundo gradiente
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                    )
                                ),
                                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                            )
                            .padding(vertical = 24.dp, horizontal = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Conteúdo do Karate",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Explore o conteúdo de cada faixa e aprimore suas técnicas",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    // Conteúdo principal com grid de faixas
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .offset(y = (-20).dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Selecione uma faixa",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            val availableFaixas = uiState.faixas.sortedBy { it.ordem }

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(availableFaixas) { faixa ->
                                    FaixaCard(
                                        faixa = faixa.faixa,
                                        onClick = {
                                            themeViewModel.changeThemeFaixa(faixa.faixa)
                                            onClickFaixa(faixa.faixa)
                                        },
                                        isDarkTheme = isSystemInDarkTheme()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FaixaCard(
    faixa: String,
    onClick: () -> Unit,
    isDarkTheme: Boolean
) {
    val faixaColor = selecionaCorIcone(faixa, isDarkTheme)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val iconPainter = if (faixa == "Branca" && !isDarkTheme) {
                painterResource(id = R.drawable.ic_faixa_outline)
            } else {
                painterResource(id = R.drawable.ic_faixa)
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(faixaColor.copy(alpha = 0.2f))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = iconPainter,
                    contentDescription = "Faixa $faixa",
                    tint = faixaColor,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = faixa,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun selecionaCorIcone(faixa: String, isDarkTheme: Boolean): Color {
    return when (faixa) {
        "Branca" -> if (isDarkTheme) Color.White else Color.Black
        "Amarela" -> LightPrimaryContainerColorAmarela
        "Laranja" -> PrimaryColorLaranja
        "Verde" -> PrimaryColorVerde
        "Roxa" -> PrimaryColorRoxa
        "Marrom" -> PrimaryColorMarron
        "Preta", "Preta 1 dan", "Preta 2 dan", "Preta 3 dan", "Preta 4 dan" -> PrimaryColorPreta
        "Mestre", "Mestre 6 dan", "Mestre 7 dan", "Mestre 8 dan", "Mestre 9 dan" -> PrimaryColorMestre
        "Grão Mestre", "Grão Mestre 11 dan", "Grão Mestre 12 dan" -> PrimaryColorGraoMestre
        else -> Color(0xFF8A2BE2)
    }
}
