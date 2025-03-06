package br.com.shubudo.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
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
import br.com.shubudo.ui.components.LoadingOverlay
import br.com.shubudo.ui.uistate.AvisoUiState
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun String.formatDate(): String {
    return try {
        val zdt = ZonedDateTime.parse(this)
        zdt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    } catch (e: DateTimeParseException) {
        this
    }
}

@Composable
fun AvisosView(
    uiState: AvisoUiState,
    onAvisoClick: (Aviso) -> Unit,
    onAddAvisoClick: () -> Unit,
    onReload: () -> Unit
) {
    when (uiState) {
        is AvisoUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Cabeçalho de fundo
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
                ) { }
                LoadingOverlay(true) {}
            }
        }
        is AvisoUiState.Success, is AvisoUiState.Empty -> {
            // Se Success, usamos a lista de avisos; se Empty, lista vazia.
            val avisosList = when (uiState) {
                is AvisoUiState.Success -> uiState.avisos
                is AvisoUiState.Empty -> emptyList()
                else -> emptyList()
            }
            Column(modifier = Modifier.fillMaxSize()) {
                // Cabeçalho com fundo colorido atrás do texto
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Mantenha-se informado",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Barra de ações (Recarregar e Adicionar)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onReload) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Recarregar avisos"
                        )
                    }
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
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // Se a lista estiver vazia, exibe mensagem; caso contrário, exibe os itens.
                if (avisosList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhum aviso disponível no momento!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 8.dp, start = 16.dp, end = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(avisosList) { aviso ->
                            if (aviso.exclusivoParaFaixas.contains(SessionManager.usuarioLogado?.corFaixa)
                                || aviso.exclusivoParaFaixas.isEmpty()
                            ) {
                                AvisoItem(
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

@Composable
fun AvisoItem(
    aviso: Aviso,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Barra vertical com a cor primária
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(color = MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = if (aviso.dataCriacao.isNotBlank()) aviso.dataCriacao.formatDate() else "15/03/2024",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Text(
                text = aviso.titulo,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = aviso.conteudo,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
