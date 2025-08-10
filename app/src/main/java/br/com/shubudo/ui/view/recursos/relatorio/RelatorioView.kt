package br.com.shubudo.ui.view.recursos.relatorio

import CampoDeTextoPadrao
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.SessionManager.perfilAtivo
import br.com.shubudo.ui.components.CabecalhoComIconeCentralizado
import br.com.shubudo.ui.uistate.RelatorioUiState
import br.com.shubudo.ui.viewModel.RelatorioViewModel
import br.com.shubudo.utils.formatarDataHoraLocal

data class ReportAction(
    val titulo: String,
    val descricao: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true
)

@Composable
fun RelatoriosView(
    viewModel: RelatorioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    var fluxo = ""

    // Feedback após sucesso/erro
    LaunchedEffect(uiState) {
        when (uiState) {
            is RelatorioUiState.Success -> {
                val name = (uiState as RelatorioUiState.Success).fileName
                snackbar.showSnackbar("Relatório salvo em Downloads: $name")

                viewModel.reset()
            }

            is RelatorioUiState.Error -> {
                val msg = (uiState as RelatorioUiState.Error).message
                snackbar.showSnackbar("Erro: $msg")
                viewModel.reset()
            }

            else -> Unit
        }
    }

    val isDownloading = uiState is RelatorioUiState.Downloading

    // Defina os botões (relatórios) aqui
    val acoes = mutableListOf<ReportAction>()

    if (perfilAtivo == "adm") {
        acoes.add(
            ReportAction(
                titulo = "Distribuição Exame",
                descricao = "Gera planilha com cones/filas e chamadas por faixa, altura e academia.",
                onClick = {
                    fluxo = "exame"
                    viewModel.abrirModalRelatorioEvento(fluxo)
                },
                enabled = !isDownloading
            )
        )
    }

    acoes.add(
        ReportAction(
            titulo = "Extração Exame de Faixa",
            descricao = "Gera planilha de exame a partir da lista de confirmados do evento.",
            onClick = {
                fluxo = "requirimento"
                viewModel.abrirModalRelatorioEvento(fluxo)
            },
            enabled = !isDownloading
        )
    )


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbar) }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                CabecalhoComIconeCentralizado(
                    titulo = "Relatórios",
                    subtitulo = "Gere planilhas direto do app",
                    iconeAndroid = Icons.Default.Assessment
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
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Ações rápidas",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (isDownloading) {
                            Spacer(Modifier.height(12.dp))
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Baixando relatório...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(acoes) { acao ->
                                ReportActionCard(acao = acao)
                            }
                        }
                    }
                }
            }
        }

        val mostrarModal by viewModel.mostrarModalRelatorio.collectAsState()

        if (mostrarModal) {
            ModalRelatorioEventoDialog(
                onDismiss = { viewModel.fecharModalRelatorioEvento() },
                fluxo = fluxo,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun ModalRelatorioEventoDialog(
    onDismiss: () -> Unit,
    fluxo: String,
    viewModel: RelatorioViewModel
) {
    val eventos by viewModel.eventosDisponiveis.collectAsState()
    var isPrimeiraInfancia by remember { mutableStateOf(false) }
    var cones by remember { mutableStateOf("1") }
    var filas by remember { mutableStateOf("A") }
    var selecionadoId by remember { mutableStateOf<String?>(null) }
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val focusCone = remember { FocusRequester() }
    val focusFila = remember { FocusRequester() }

    // Filtrar eventos baseado na pesquisa
    val eventosFiltrados = remember(eventos, searchText) {
        if (searchText.isBlank()) {
            eventos
        } else {
            eventos.filter { evento ->
                evento.titulo.contains(searchText, ignoreCase = true) ||
                        formatarDataHoraLocal(evento.dataInicio, false).contains(
                            searchText,
                            ignoreCase = true
                        )
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            modifier = Modifier.fillMaxWidth(0.98f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // Cabeçalho da modal
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Extração por evento",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Selecione o evento para gerar o relatório",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))
                HorizontalDivider(
                    Modifier,
                    DividerDefaults.Thickness,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
                Spacer(Modifier.height(20.dp))
                if (fluxo == "exame") {

                    CampoDeTextoPadrao(
                        value = cones,
                        onValueChange = { cones = it},
                        label = "Cone maximo",
                        placeholder = "Digite o cone",
                        leadingIcon = Icons.Default.SwapHoriz,
                        focusRequester = focusCone,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                        )
                    )
                    Spacer(Modifier.height(8.dp))

                    CampoDeTextoPadrao(
                        value = filas,
                        onValueChange = { filas = it },
                        label = "Fila maxima",
                        placeholder = "Digite a fila",
                        leadingIcon = Icons.Default.SortByAlpha,
                        focusRequester = focusFila,
                    )
                }
                // Switch Primeira Infância
                if (fluxo == "requirimento") {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        onClick = { isPrimeiraInfancia = !isPrimeiraInfancia },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Primeira infância",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Ativar para gerar arquivo de primeira infância",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                            Switch(
                                checked = isPrimeiraInfancia,
                                onCheckedChange = { isPrimeiraInfancia = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                )
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Campo de pesquisa
                Text(
                    text = "Pesquisar eventos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = {
                        Text(
                            "Digite o nome do evento...",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Pesquisar",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    searchText = ""
                                    keyboardController?.hide()
                                }
                            ) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "Limpar",
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = { keyboardController?.hide() }
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )

                Spacer(Modifier.height(16.dp))

                // Lista de eventos
                Text(
                    text = if (eventosFiltrados.isEmpty() && searchText.isNotEmpty()) {
                        "Nenhum evento encontrado"
                    } else {
                        "Eventos disponíveis (${eventosFiltrados.size})"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = if (eventosFiltrados.isEmpty() && searchText.isNotEmpty()) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )

                if (eventosFiltrados.isEmpty() && searchText.isNotEmpty()) {
                    Text(
                        text = "Tente pesquisar com outros termos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(eventosFiltrados.size) { idx ->
                        val ev = eventosFiltrados[idx]
                        val selected = selecionadoId == ev._id
                        EventRowItem(
                            titulo = ev.titulo,
                            dataIso = ev.dataInicio,
                            selected = selected,
                            onClick = {
                                selecionadoId = ev._id
                                keyboardController?.hide()
                            }
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))
                HorizontalDivider(
                    Modifier,
                    DividerDefaults.Thickness,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
                Spacer(Modifier.height(16.dp))

                // Botões de ação
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.Medium)
                    }
                    Button(
                        onClick = {
                            selecionadoId?.let {
                                if (fluxo == "requirimento") {
                                    viewModel.gerarRelatorioExamePorEvento(
                                        it,
                                        isPrimeiraInfancia,
                                        context
                                    )
                                } else if (fluxo == "exame") {
                                    viewModel.baixarRelatorioOrganizado(context, it, cones, filas)
                                }
                            }
                            viewModel.fecharModalRelatorioEvento()
                            keyboardController?.hide()
                        },
                        enabled = selecionadoId != null,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text("Gerar Relatório", fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
private fun EventRowItem(
    titulo: String,
    dataIso: String?,
    selected: Boolean,
    onClick: () -> Unit
) {
    // Formata a data ISO (ex.: 2025-08-17T15:00:00.000+00:00) em algo curto
    val dataFmt = remember(dataIso) { formatarDataCurta(dataIso) }

    val containerColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 6.dp else 2.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador visual de seleção
            if (selected) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.size(8.dp)
                ) {}
                Spacer(Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    color = contentColor,
                    maxLines = 2
                )
                if (!dataFmt.isNullOrBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = dataFmt,
                        style = MaterialTheme.typography.bodySmall,
                        color = contentColor.copy(alpha = 0.7f)
                    )
                }
            }

            if (selected) {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = "Selecionado",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

private fun formatarDataCurta(iso: String?): String? {
    if (iso.isNullOrBlank()) return null
    return try {
        // Usa java.time quando disponível
        java.time.OffsetDateTime.parse(iso)
            .toLocalDateTime()
            .let { dt ->
                "${dt.dayOfMonth.toString().padStart(2, '0')}/${
                    dt.monthValue.toString().padStart(2, '0')
                }/${dt.year}"
            }
    } catch (_: Exception) {
        null
    }
}


@Composable
private fun ReportActionCard(
    acao: ReportAction
) {
    Card(
        onClick = { if (acao.enabled) acao.onClick() },
        enabled = acao.enabled,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(Modifier.height(8.dp))
            Text(text = acao.titulo, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(
                text = acao.descricao,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 5
            )
        }
    }
}
