package br.com.shubudo.ui.view.recursos.avisos

import LoadingButton
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.model.Usuario
import br.com.shubudo.ui.components.LoadingOverlay
import br.com.shubudo.ui.uistate.CadastroAvisoUiState
import br.com.shubudo.ui.viewModel.CadastroAvisoViewModel
import br.com.shubudo.utils.getCorDaFaixa
import br.com.shubudo.utils.getCorOnPrimary

@Composable
fun CadastroAvisoView(
    avisoId: String = "",
    onNavigateBack: () -> Unit,
    viewModel: CadastroAvisoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var titulo by remember { mutableStateOf("") }
    var conteudo by remember { mutableStateOf("") }
    var searchText by remember { mutableStateOf("") }
    var usuariosSelecionados by remember { mutableStateOf(setOf<Usuario>()) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showUserDropdown by remember { mutableStateOf(false) }
    var dadosPreenchidos by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadUsuarios()
    }

    LaunchedEffect(avisoId) {
        if (avisoId.isNotEmpty()) {
            viewModel.carregarAviso(avisoId)
        }
    }

    LaunchedEffect(uiState, dadosPreenchidos) {
        val state = uiState
        if (
            state is CadastroAvisoUiState.Success &&
            state.isEditando &&
            !dadosPreenchidos
        ) {
            titulo = state.titulo
            conteudo = state.conteudo
            usuariosSelecionados = state.usuariosSelecionados
            dadosPreenchidos = true
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Cabeçalho
        Log.d("CadastroAvisoView", "usuários selecionados: $usuariosSelecionados")
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "Novo Aviso",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Criar um novo aviso para os usuários",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // Conteúdo principal
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
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Campo Título
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Campo Conteúdo
                OutlinedTextField(
                    value = conteudo,
                    onValueChange = { conteudo = it },
                    label = { Text("Conteúdo *") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    maxLines = 8
                )

                // Seção Público Alvo
                Text(
                    text = "Público Alvo",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                // Campo de busca de usuários
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        viewModel.searchUsuarios(it)
                        showUserDropdown = it.isNotEmpty()
                    },
                    label = { Text("Buscar usuários") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    searchText = ""
                                    showUserDropdown = false
                                    viewModel.searchUsuarios("")
                                }
                            ) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpar")
                            }
                        }
                    }
                )

                // Lista de usuários filtrados
                if (showUserDropdown && uiState is CadastroAvisoUiState.Success) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            val usuariosFiltrados =
                                (uiState as? CadastroAvisoUiState.Success)?.usuariosFiltrados
                                    ?: emptyList()
                            items(usuariosFiltrados) { usuario ->

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            usuariosSelecionados =
                                                if (usuariosSelecionados.contains(usuario)) {
                                                    usuariosSelecionados - usuario
                                                } else {
                                                    usuariosSelecionados + usuario
                                                }
                                        }
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = usuariosSelecionados.contains(usuario),
                                        onCheckedChange = { checked ->
                                            usuariosSelecionados = if (checked) {
                                                usuariosSelecionados + usuario
                                            } else {
                                                usuariosSelecionados - usuario
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = usuario.nome,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = "${usuario.email} • ${usuario.corFaixa}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Text(
                    text = "Atalhos por faixa:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                )
                {
                    listOf(
                        "Branca",
                        "Amarela",
                        "Laranja",
                        "Verde",
                        "Roxa",
                        "Marrom",
                        "Preta",
                        "Mestre"
                    ).forEach { faixa ->
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = getCorDaFaixa(faixa),
                                contentColor = getCorOnPrimary(faixa)
                            ),

                            onClick = {
                                val selecionados = viewModel.selecionarUsuariosPorFaixa(faixa)
                                usuariosSelecionados = usuariosSelecionados + selecionados
                            }) {
                            Text(faixa)
                        }
                    }
                }

                // Usuários selecionados
                if (usuariosSelecionados.isNotEmpty()) {
                    Text(
                        text = "Usuários selecionados (${usuariosSelecionados.size})",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    usuariosSelecionados.forEach { usuario ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = getCorDaFaixa(usuario.corFaixa),
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = usuario.nome,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = getCorOnPrimary(usuario.corFaixa)
                                    )
                                    Text(
                                        text = buildString {
                                            append("${usuario.email} • ${usuario.corFaixa}")
                                            if (usuario.dan > 0) {
                                                append("${usuario.dan}º Dan")
                                            }
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = getCorOnPrimary(usuario.corFaixa).copy(alpha = 0.7f)
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        usuariosSelecionados = usuariosSelecionados - usuario
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remover",
                                        tint = getCorOnPrimary(usuario.corFaixa)
                                    )
                                }
                            }
                        }
                    }
                }

                // Aviso sobre público alvo vazio
                if (usuariosSelecionados.isEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Se nenhum usuário for selecionado, o aviso será enviado para todos os usuários ativos.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botão Enviar
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = titulo.isNotBlank() && conteudo.isNotBlank()
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enviar Aviso")
                }
            }
        }
    }

    // Dialog de confirmação
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirmar Envio") },
            text = {
                Column {
                    Text("Deseja realmente enviar este aviso?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Título: $titulo",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (usuariosSelecionados.isEmpty()) {
                            "Será enviado para todos os usuários ativos"
                        } else {
                            "Será enviado para ${usuariosSelecionados.size} usuário(s) selecionado(s)"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            confirmButton = {
                LoadingButton(
                    onClick = {
                        val publicoAlvo = usuariosSelecionados.map { it.email }
                        showLoading = true
                        viewModel.criarOuAtualizarAviso(
                            titulo = titulo,
                            conteudo = conteudo,
                            publicoAlvo = publicoAlvo,
                            onSuccess = {
                                showConfirmDialog = false
                                showLoading = false
                                onNavigateBack()
                            },
                            onError = {
                                showConfirmDialog = false
                                showLoading = false
                            }
                        )
                    },
                    isLoading = showLoading,
                    text = "Confirmar",
                    loadingText = "Enviando..."
                )
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Loading overlay
    if (uiState is CadastroAvisoUiState.Loading) {
        LoadingOverlay(true) {}
    }
}