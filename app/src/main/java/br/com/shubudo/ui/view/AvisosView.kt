package br.com.shubudo.ui.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.SessionManager
import br.com.shubudo.model.Aviso
import br.com.shubudo.ui.components.CardAviso
import br.com.shubudo.ui.components.LoadingOverlay
import br.com.shubudo.ui.uistate.AvisoUiState
import br.com.shubudo.ui.viewModel.ThemeViewModel

@Composable
fun AvisosView(
    uiState: AvisoUiState,
    onAvisoClick: (Aviso) -> Unit,
    onAddAvisoClick: () -> Unit,
    onReload: () -> Unit
) {
    when (uiState) {
        is AvisoUiState.Success -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                // Top Row: Título + Botão de Reload
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Confira os avisos recentes e mantenha-se informado!",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.weight(1f)
                    )

                }
                // Conteúdo principal com os avisos
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 28.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                        modifier = Modifier
                            .border(
                                width = 0.dp,
                                color = Color.Transparent,
                                shape = RoundedCornerShape(26.dp)
                            )
                            .padding(16.dp)
                            .fillMaxSize(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row (
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ){
                                IconButton(onClick = onReload) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Recarregar avisos"
                                    )
                                }
                                // Exibe o botão de adicionar aviso somente se o perfil for "adm"
                                if (SessionManager.usuarioLogado?.perfil?.equals("adm", ignoreCase = true) == true) {
                                    IconButton(onClick = onAddAvisoClick) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Adicionar Aviso"
                                        )
                                    }
                                }
                            }


                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(top = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(uiState.avisos) { aviso ->
                                    if (aviso.exclusivoParaFaixas.contains(SessionManager.usuarioLogado?.corFaixa)
                                        || aviso.exclusivoParaFaixas.isEmpty()
                                    ) {
                                        CardAviso(
                                            aviso = aviso,
                                            onClick = { onAvisoClick(aviso) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        is AvisoUiState.Empty -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum aviso disponível no momento!",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        is AvisoUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
                ) { }
                LoadingOverlay(true) { }
            }
        }
    }
}
