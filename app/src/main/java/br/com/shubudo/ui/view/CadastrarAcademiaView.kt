package br.com.shubudo.ui.view

import android.util.Log
import androidx.compose.foundation.background
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
import br.com.shubudo.model.Filial
import br.com.shubudo.ui.uistate.CadastroAcademiaUiState
import br.com.shubudo.ui.viewModel.CadastroAcademiaViewModel

@Composable
fun CadastroAcademiaView(
    academiaId: String = "",
    onNavigateBack: () -> Unit,
    viewModel: CadastroAcademiaViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()

    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var filiais by remember { mutableStateOf(listOf<Filial>()) }
    var showDialog by remember { mutableStateOf(false) }

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
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                        )
                    ),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(24.dp)
        ) {
            Row(
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
                .padding(16.dp)
                .verticalScroll(scrollState)
                .offset(y = (-20).dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome da Academia *") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Filiais",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                filiais.forEachIndexed { index, filial ->
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        OutlinedTextField(
                            value = filial.nome,
                            onValueChange = { newNome ->
                                filiais = filiais.toMutableList().apply {
                                    this[index] = this[index].copy(nome = newNome)
                                }
                            },
                            label = { Text("Nome da Filial") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = filial.endereco,
                            onValueChange = { newEndereco ->
                                filiais = filiais.toMutableList().apply {
                                    this[index] = this[index].copy(endereco = newEndereco)
                                }
                            },
                            label = { Text("Endereço da Filial") },
                            modifier = Modifier.fillMaxWidth()
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
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar Cadastro") },
            text = { Text("Deseja realmente cadastrar esta academia?") },
            confirmButton = {
                Button(
                    onClick = {

                        Log.d("CadastroAcademiaView", "Cadastrando academia: $academiaId")
                        viewModel.criarOuAtualizarAcademia(
                            nome = nome,
                            descricao = descricao.takeIf { it.isNotBlank() },
                            filiais = filiais,
                            id = academiaId,
                            onSuccess = {
                                showDialog = false
                                onNavigateBack()
                            },
                            onError = {}
                        )

                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
