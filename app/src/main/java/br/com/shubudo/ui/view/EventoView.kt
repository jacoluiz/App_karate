package br.com.shubudo.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.SessionManager
import br.com.shubudo.model.Evento
import br.com.shubudo.ui.components.LoadingOverlay
import br.com.shubudo.ui.uistate.EventoUiState
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

fun String.isPastEvent(): Boolean {
    return try {
        ZonedDateTime.parse(this).isBefore(ZonedDateTime.now())
    } catch (e: DateTimeParseException) {
        false
    }
}

fun String.formatDateTime(): String {
    return try {
        val zdt = ZonedDateTime.parse(this)
        zdt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
    } catch (e: DateTimeParseException) {
        this
    }
}

@Composable
fun EventosView(
    uiState: EventoUiState,
    onReload: () -> Unit,
    onAddEventoClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    when (uiState) {
        is EventoUiState.Loading -> {
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
                ) {}
                LoadingOverlay(true) {}
            }
        }

        is EventoUiState.Success, is EventoUiState.Empty -> {
            val futuros = mutableListOf<Evento>()
            val passados = mutableListOf<Evento>()

            if (uiState is EventoUiState.Success) {
                uiState.eventosAgrupados.values.flatten().forEach { evento ->
                    if (evento.dataInicio.isPastEvent()) {
                        passados.add(evento)
                    } else {
                        futuros.add(evento)
                    }
                }
            }

            val filteredFuturos = futuros.filter {
                it.titulo.contains(searchQuery.text, ignoreCase = true) ||
                        it.descricao.contains(searchQuery.text, ignoreCase = true)
            }
            val filteredPassados = passados.filter {
                it.titulo.contains(searchQuery.text, ignoreCase = true) ||
                        it.descricao.contains(searchQuery.text, ignoreCase = true)
            }

            Column(modifier = Modifier.fillMaxSize()) {

                // Topo colorido
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
                            text = "Próximos Eventos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Ações
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
                            contentDescription = "Recarregar eventos"
                        )
                    }
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Filtrar eventos...") },
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                    )
                    if (SessionManager.usuarioLogado?.perfil?.equals("adm", ignoreCase = true) == true) {
                        IconButton(onClick = onAddEventoClick) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Adicionar Evento"
                            )
                        }
                    }
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                if (filteredFuturos.isEmpty() && filteredPassados.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhum evento encontrado!",
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
                        if (filteredFuturos.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Próximos Eventos",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                            items(filteredFuturos) { evento ->
                                EventoItem(evento = evento, isPast = false)
                            }
                        }

                        if (filteredPassados.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Eventos Anteriores",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                            items(filteredPassados) { evento ->
                                EventoItem(evento = evento, isPast = true)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventoItem(evento: Evento, isPast: Boolean = false) {
    val textColor = if (isPast) Color.LightGray else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(color = if (isPast) Color.Gray else MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = evento.titulo,
                style = MaterialTheme.typography.titleMedium,
                color = textColor,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = evento.dataInicio.formatDateTime(),
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
            Text(
                text = evento.descricao,
                style = MaterialTheme.typography.bodySmall,
                color = textColor
            )
            Text(
                text = evento.local,
                style = MaterialTheme.typography.labelSmall,
                color = textColor
            )
        }
    }
}
