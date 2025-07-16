package br.com.shubudo.ui.view.detalheMovimentoView.MovimentosPadrao

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.shubudo.model.Movimento
import br.com.shubudo.ui.components.BotaoVoltar
import br.com.shubudo.ui.components.itemDetalheMovimento
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.utils.toOrdinarioFeminino

@Composable
fun TelaListaMovimentoPadrao(
    uiState: DetalheMovimentoUiState.Success,
    onBackNavigationClick: () -> Unit = {},
    onCardClick: (movimento: Movimento) -> Unit
) {
    val listState = rememberLazyListState()
    
    LazyColumn(
        state = listState,
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.padding(top = 46.dp))
            Text(
                text = "Movimentos",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        
        items(uiState.movimento) { movimento ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                onClick = { onCardClick(movimento) }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = movimento.nome,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = movimento.ordem.toOrdinarioFeminino(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        movimento.tipoMovimento?.let {
                            itemDetalheMovimento(
                                descricao = "",
                                valor = it,
                                icone = Icons.Default.Apps
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        
                        itemDetalheMovimento(
                            descricao = "Base",
                            valor = movimento.base,
                            icone = Icons.Default.Accessibility
                        )
                    }
                    
                    // Preview of description (first 80 chars)
                    if (movimento.descricao.isNotEmpty()) {
                        Text(
                            text = movimento.descricao.take(80) + if (movimento.descricao.length > 80) "..." else "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
    
    BotaoVoltar(
        listState = listState,
        onBackNavigationClick = onBackNavigationClick
    )
}