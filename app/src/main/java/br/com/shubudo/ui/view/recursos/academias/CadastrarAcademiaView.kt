package br.com.shubudo.ui.view.recursos.academias

import CampoDeTextoPadrao
import LoadingButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.model.Filial
import br.com.shubudo.ui.uistate.CadastroAcademiaUiState
import br.com.shubudo.ui.viewModel.CadastroAcademiaViewModel

@Composable
fun CadastroAcademiaView(
    academiaId: String = "",
    onNavigateBack: () -> Unit,
    viewModel: CadastroAcademiaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var filiais by remember { mutableStateOf(listOf<Filial>()) }
    var showDialog by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }
    val focusNome = remember { FocusRequester() }
    val focusDescricao = remember { FocusRequester() }

    val camposValidos = nome.isNotBlank()
            && descricao.isNotBlank()
            && filiais.isNotEmpty()
            && filiais.all { it.nome.isNotBlank() && it.endereco.isNotBlank() }

    LaunchedEffect(uiState) {
        if (uiState is CadastroAcademiaUiState.Success) {
            val data = uiState as CadastroAcademiaUiState.Success
            nome = data.nome
            descricao = data.descricao
            filiais = data.filiais
        }
    }

    LaunchedEffect(academiaId) {
        if (academiaId.isNotBlank()) {
            viewModel.carregarAcademia(academiaId)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Cabeçalho
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
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        "Nova Academia",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        "Cadastre uma nova academia",
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
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
                CampoDeTextoPadrao(
                    value = nome,
                    onValueChange = { nome = it },
                    label = "Nome da Academia *",
                    placeholder = "Digite o nome",
                    leadingIcon = Icons.Default.Add,
                    focusRequester = focusNome,
                )

                CampoDeTextoPadrao(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = "Descrição (opcional)",
                    placeholder = "Breve descrição",
                    leadingIcon = Icons.Default.Add,
                    focusRequester = focusDescricao
                )

                Text(
                    "Filiais",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                filiais.forEachIndexed { index, filial ->
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        CampoDeTextoPadrao(
                            value = filial.nome,
                            onValueChange = { newNome ->
                                filiais = filiais.toMutableList().apply {
                                    this[index] = this[index].copy(nome = newNome)
                                }
                            },
                            label = "Nome da Filial",
                            placeholder = "Digite o nome da filial",
                            leadingIcon = Icons.Default.Add,
                            focusRequester = remember { FocusRequester() }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        CampoDeTextoPadrao(
                            value = filial.endereco,
                            onValueChange = { newEndereco ->
                                filiais = filiais.toMutableList().apply {
                                    this[index] = this[index].copy(endereco = newEndereco)
                                }
                            },
                            label = "Endereço da Filial",
                            placeholder = "Digite o endereço",
                            leadingIcon = Icons.Default.Add,
                            focusRequester = remember { FocusRequester() }
                        )

                        TextButton(
                            onClick = {
                                filiais = filiais.toMutableList().apply {
                                    removeAt(index)
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Remover")
                            Text("Remover", modifier = Modifier.padding(start = 4.dp))
                        }
                    }
                }

                TextButton(
                    onClick = {
                        filiais = filiais.toMutableList().apply {
                            add(Filial(nome = "", endereco = ""))
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar Filial")
                    Text("Adicionar Filial", modifier = Modifier.padding(start = 8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = camposValidos,
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Salvar Academia")
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showLoading = false
                showDialog = false
            },
            title = { Text("Confirmar Cadastro") },
            text = { Text("Deseja realmente cadastrar esta academia?") },
            confirmButton = {
                LoadingButton(
                    onClick = {
                        showLoading = true
                        showDialog = true
                        viewModel.criarOuAtualizarAcademia(
                            nome = nome,
                            descricao = descricao.takeIf { it.isNotBlank() },
                            filiais = filiais,
                            id = academiaId,
                            onSuccess = {
                                showDialog = false
                                showLoading = false
                                onNavigateBack()
                            },
                            onError = {
                                showLoading = false
                            }
                        )
                    },
                    isLoading = showLoading,
                    text = "Confirmar",
                    loadingText = "Salvando...",
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(48.dp)
                )
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
