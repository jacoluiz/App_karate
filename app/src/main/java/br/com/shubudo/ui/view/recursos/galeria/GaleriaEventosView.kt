package br.com.shubudo.ui.view.recursos.galeria

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.R
import br.com.shubudo.SessionManager.perfilAtivo
import br.com.shubudo.SessionManager.usuarioLogado
import br.com.shubudo.model.GaleriaEvento
import br.com.shubudo.ui.components.CabecalhoComIconeCentralizado
import br.com.shubudo.ui.components.LoadingWrapper
import br.com.shubudo.ui.uistate.GaleriaEventosUiState
import br.com.shubudo.ui.view.recursos.eventos.SectionHeader
import br.com.shubudo.ui.viewModel.GaleriaEventosViewModel
import br.com.shubudo.utils.formatarDataHoraLocal

@Composable
fun GaleriaEventosView(
    viewModel: GaleriaEventosViewModel = hiltViewModel(),
    onAddEventoClick: () -> Unit = {},
    onEditEventoClick: (String) -> Unit = {},
    onClickEvento: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    var searchQuery by remember { mutableStateOf(TextFieldValue()) }

    LoadingWrapper(isLoading = uiState is GaleriaEventosUiState.Loading) {
        Column(modifier = Modifier.fillMaxSize()) {
            CabecalhoComIconeCentralizado(
                titulo = "Galeria",
                subtitulo = "Todas as nossas fotos organizadas em um só lugar!",
                iconePastaR = R.drawable.ic_galeria
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState is GaleriaEventosUiState.Success) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                "Buscar álbuns...",
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
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (uiState) {
                    is GaleriaEventosUiState.Success -> {
                        val eventosFiltrados =
                            (uiState as GaleriaEventosUiState.Success).eventos.filter {
                                it.titulo.contains(searchQuery.text, ignoreCase = true) ||
                                        it.descricao?.contains(
                                            searchQuery.text,
                                            ignoreCase = true
                                        ) == true ||
                                        formatarDataHoraLocal(
                                            it.data,
                                            false
                                        ).contains(searchQuery.text, ignoreCase = true)
                            }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SectionHeader(
                                    title = "Selecione o álbun",
                                    subtitle = "${eventosFiltrados.size} evento${if (eventosFiltrados.size != 1) "s" else ""}",
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

                        items(eventosFiltrados) { evento ->
                            GaleriaEventoItem(
                                evento = evento,
                                onClick = { onClickEvento(evento._id) },
                                onEditClick = { onEditEventoClick(evento._id) },
                                onDeleteClick = { evento._id.let { viewModel.deletarEvento(evento._id) } },
                                viewModel = viewModel,
                            )
                        }
                    }

                    GaleriaEventosUiState.Empty -> {
                        item {

                            Column(modifier = Modifier.fillMaxSize()) {
                                // Conteúdo vazio
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
                                            text = "Nenhum álbum disponível no momento",
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        if (usuarioLogado?.perfis?.contains("adm") == true || perfilAtivo == "professor") {
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Button(
                                                onClick = {
                                                    onAddEventoClick()
                                                }) {
                                                Icon(Icons.Default.Add, contentDescription = null)
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Criar Primeiro Álbum")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
fun GaleriaEventoItem(
    evento: GaleriaEvento,
    onClick: () -> Unit = {},
    onEditClick: (String) -> Unit = {},
    onDeleteClick: (String) -> Unit = {},
    viewModel: GaleriaEventosViewModel,
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val numeroFotos by produceState(initialValue = 0) {
        value = viewModel.numeroDeFotos(evento._id)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = evento.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (usuarioLogado?.perfis?.contains("adm") == true || perfilAtivo == "professor") {
                        Box {
                            IconButton(
                                onClick = { showMenu = true },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Mais opções",
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
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
                                                imageVector = Icons.Default.Edit, // Adicione o ícone edit (você pode importar se não estiver)
                                                contentDescription = null,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Editar álbun")
                                        }
                                    },
                                    onClick = {
                                        showMenu = false
                                        onEditClick(evento._id) // chama callback de edição
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
                                                "Excluir álbun",
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
            }

            Spacer(modifier = Modifier.height(4.dp))
            evento.descricao?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatarDataHoraLocal(evento.data, false),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Text(
                    text = "$numeroFotos foto(s)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Confirmar Exclusão") },
                text = {
                    Column {
                        Text("Deseja realmente excluir este álbun?")
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
                            onDeleteClick(evento._id)
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
    }
}
