package br.com.shubudo.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
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
                    modifier = Modifier.padding(horizontal = 32.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        is ProgramacaoUiState.Success -> {
            Column(modifier = Modifier.fillMaxSize()) {
                // Cabeçalho com fundo gradiente
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                )
                            ),
                            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                        )
                        .padding(vertical = 24.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center
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
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        // Grid de faixas
                        val userFaixa = SessionManager.usuarioLogado?.corFaixa?.trim()?.lowercase() ?: ""
                        val availableFaixas = uiState.faixas.filter { faixa ->
                            val faixaNome = faixa.faixa.trim().lowercase()
                            userFaixa in FAIXAS_BLOQUEADAS || (userFaixa !in FAIXAS_BLOQUEADAS && faixaNome !in FAIXAS_BLOQUEADAS)
                        }.sortedBy { it.ordem }
                        
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
            // Faixa icon
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