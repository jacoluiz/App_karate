package br.com.shubudo.ui.view.recursos.eventos

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.SessionManager.perfilAtivo
import br.com.shubudo.SessionManager.usuarioLogado
import br.com.shubudo.model.Evento
import br.com.shubudo.model.Usuario
import br.com.shubudo.ui.components.CabecalhoComIconeCentralizado
import br.com.shubudo.ui.components.LoadingWrapper
import br.com.shubudo.ui.uistate.EventosUiState
import br.com.shubudo.ui.viewModel.components.UsuarioListViewModel
import br.com.shubudo.utils.getCorDaFaixa
import br.com.shubudo.utils.getCorOnPrimary
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

fun String.formatDate(): String {
    return try {
        val zdt = ZonedDateTime.parse(this)
        zdt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    } catch (_: DateTimeParseException) {
        this
    }
}

fun String.formatTime(): String {
    return try {
        val zdt = ZonedDateTime.parse(this)
        zdt.format(DateTimeFormatter.ofPattern("HH:mm"))
    } catch (_: DateTimeParseException) {
        this
    }
}

fun String.isPastEvent(): Boolean {
    return try {
        ZonedDateTime.parse(this).isBefore(ZonedDateTime.now())
    } catch (_: DateTimeParseException) {
        false
    }
}

fun String.formatDayMonth(): String {
    return try {
        val zdt = ZonedDateTime.parse(this)
        zdt.format(DateTimeFormatter.ofPattern("dd\nMMM", Locale("pt", "BR")))
    } catch (_: DateTimeParseException) {
        this
    }
}

fun String.getDiaSemanaAbreviado(): String {
    return try {
        val zdt = ZonedDateTime.parse(this)
        zdt.format(DateTimeFormatter.ofPattern("EEE", Locale("pt", "BR")))
    } catch (_: DateTimeParseException) {
        this
    }
}

@Composable
fun EventosView(
    uiState: EventosUiState,
    onReload: () -> Unit = {},
    onEventClick: (String) -> Unit = {},
    onAddEventoClick: () -> Unit = {},
    onEditEventoClick: (String) -> Unit = {},
    onDeleteEvento: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue()) }
    val usuarioListViewModel: UsuarioListViewModel = hiltViewModel()
    val usuarios by usuarioListViewModel.usuarios.collectAsState()


    LoadingWrapper(
        isLoading = uiState is EventosUiState.Loading,
        loadingText = "Carregando eventos do karate..."
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (uiState) {
                is EventosUiState.Loading -> {

                }

                is EventosUiState.Empty -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        CabecalhoComIconeCentralizado(
                            titulo = "Eventos do karatê",
                            subtitulo = "Fique por dentro de todas as atividades",
                            iconeAndroid = Icons.Default.Event
                        )

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
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Nenhum evento disponível no momento",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                if (usuarioLogado?.perfis?.contains("adm") == true || perfilAtivo == "professor") {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(onClick = { onAddEventoClick() }) {
                                        Icon(Icons.Default.Add, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Criar Primeiro Evento")
                                    }
                                }
                            }
                        }
                    }
                }

                is EventosUiState.Success -> {
                    val futuros = mutableListOf<Evento>()
                    val passados = mutableListOf<Evento>()

                    uiState.eventosAgrupados.values.flatten().forEach { evento ->
                        if (evento.dataInicio.isPastEvent()) passados.add(evento) else futuros.add(
                            evento
                        )
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
                        CabecalhoComIconeCentralizado(
                            titulo = "Eventos do karatê",
                            subtitulo = "Fique por dentro de todas as atividades",
                            iconeAndroid = Icons.Default.Event
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    placeholder = {
                                        Text(
                                            "Buscar eventos...",
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "Buscar",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(
                                            alpha = 0.5f
                                        )
                                    ),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                IconButton(
                                    onClick = onReload,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Recarregar eventos",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (filteredFuturos.isNotEmpty()) {
                                item {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        SectionHeader(
                                            title = "Próximos Eventos",
                                            subtitle = "${filteredFuturos.size} evento${if (filteredFuturos.size != 1) "s" else ""}",
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.weight(1f)
                                        )

                                        if (usuarioLogado?.perfis?.contains("adm") == true || perfilAtivo == "professor") {
                                            FloatingActionButton(
                                                onClick = onAddEventoClick,
                                                modifier = Modifier.size(48.dp),
                                                containerColor = MaterialTheme.colorScheme.primary,
                                                contentColor = MaterialTheme.colorScheme.onPrimary
                                            ) {
                                                Icon(
                                                    Icons.Default.Add,
                                                    contentDescription = "Criar novo evento",
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                                items(filteredFuturos) { evento ->
                                    EventoItem(
                                        evento = evento,
                                        isPast = false,
                                        onClick = { onEventClick(evento._id) },
                                        isAdmin = usuarioLogado?.perfis?.contains("adm") == true || perfilAtivo == "professor",
                                        usuarios = usuarios,
                                        onDeleteEvento = onDeleteEvento,
                                        onEditEvento = onEditEventoClick
                                    )
                                }
                            }

                            if (filteredPassados.isNotEmpty()) {
                                item {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    SectionHeader(
                                        title = "Eventos Anteriores",
                                        subtitle = "${filteredPassados.size} evento${if (filteredPassados.size != 1) "s" else ""}",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                                items(filteredPassados.sortedByDescending { it.dataInicio }) { evento ->
                                    EventoItem(
                                        evento = evento,
                                        isPast = true,
                                        onClick = { onEventClick(evento._id) },
                                        isAdmin = usuarioLogado?.perfis?.contains("adm") == true || perfilAtivo == "professor",
                                        usuarios = usuarios,
                                        onDeleteEvento = onDeleteEvento,
                                        onEditEvento = onEditEventoClick
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
fun EventoItem(
    evento: Evento,
    isPast: Boolean = false,
    onClick: () -> Unit = {},
    isAdmin: Boolean = false,
    usuarios: List<Usuario> = emptyList(),
    onDeleteEvento: (String) -> Unit = {},
    onEditEvento: (String) -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showUsersDialog by remember { mutableStateOf(false) }

    val cardColor = if (isPast) {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    val textColor = if (isPast) {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            Card(
                modifier = Modifier.size(60.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isPast) {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = evento.dataInicio.formatDayMonth(),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isPast) {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        } else {
                            MaterialTheme.colorScheme.onPrimary
                        },
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = evento.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Exibe o dia da semana abreviado
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = textColor.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${evento.dataInicio.formatDate()} um ${evento.dataInicio.getDiaSemanaAbreviado()} às ${evento.dataInicio.formatTime()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Exibir localização do evento
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = textColor.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = evento.local,
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (evento.descricao.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = evento.descricao,
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor.copy(alpha = 0.8f),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Menu de 3 pontos para admin
            if (isAdmin) {
                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Mais opções",
                            tint = textColor.copy(alpha = 0.6f),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Group,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Ver confirmados")
                                }
                            },
                            onClick = {
                                showMenu = false
                                showUsersDialog = true
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Editar evento")
                                }
                            },
                            onClick = {
                                showMenu = false
                                onEditEvento(evento._id)
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Excluir evento",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            onClick = {
                                showMenu = false
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }

        }
        // Indicador de público alvo
        if (isAdmin) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                if (evento.confirmados.isNotEmpty()) {
                    Text(
                        text = "${evento.confirmados.size} usuário(s) confirmado(s)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                } else {
                    Text(
                        text = "Sem confirmações",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Dialog de confirmação de exclusão
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Confirmar Exclusão") },
                text = {
                    Column {
                        Text("Deseja realmente excluir este evento?")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Título: ${evento.titulo}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Esta ação não pode ser desfeita.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onDeleteEvento(evento._id)
                            showDeleteDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Excluir")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteDialog = false }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // Dialog de visualização de usuários confirmados
        if (showUsersDialog) {
            Dialog(
                onDismissRequest = { showUsersDialog = false }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Usuários confirmados",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        if (evento.confirmados.isEmpty()) {
                            Text(
                                text = "Nenhum usuário confirmou presença ainda.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(evento.confirmados) { email ->
                                    val usuario = usuarios.find { it.email == email }
                                    if (usuario != null) {
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = usuario.nome,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onPrimary

                                                    )
                                                    Text(
                                                        text = usuario.email,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onPrimary.copy(
                                                            alpha = 0.7f
                                                        )
                                                    )
                                                }
                                                Card(
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = getCorDaFaixa(usuario.corFaixa)
                                                    ),
                                                    modifier = Modifier.wrapContentWidth()
                                                ) {
                                                    Text(
                                                        text = usuario.corFaixa,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = getCorOnPrimary(usuario.corFaixa),
                                                        modifier = Modifier.padding(
                                                            horizontal = 8.dp,
                                                            vertical = 4.dp
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer
                                            )
                                        ) {
                                            Text(
                                                text = email,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(12.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { showUsersDialog = false }
                            ) {
                                Text("Fechar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = color.copy(alpha = 0.7f)
        )
    }
}