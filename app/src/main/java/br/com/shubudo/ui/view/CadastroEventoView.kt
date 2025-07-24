package br.com.shubudo.ui.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.ui.components.LoadingWrapper
import br.com.shubudo.ui.uistate.CadastroEventoUiState
import br.com.shubudo.ui.viewModel.CadastroEventoViewModel
import br.com.shubudo.utils.applyDateMask
import br.com.shubudo.utils.applySimpleTimeMask

@Composable
fun CadastroEventoView(
    eventoId: String? = null,
    onNavigateBack: () -> Unit = {},
    viewModel: CadastroEventoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCriarEditarDialog = remember { mutableStateOf(false) }

    LaunchedEffect(eventoId) {
        if (!eventoId.isNullOrBlank()) {
            viewModel.carregarEvento(eventoId)
        }
    }

    LoadingWrapper(
        isLoading = uiState is CadastroEventoUiState.Loading,
        loadingText = "Processando evento..."
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Cabeçalho com fundo gradiente
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (eventoId.isNullOrBlank()) "Criar novo evento" else "Editar evento",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            if (uiState is CadastroEventoUiState.Form) {
                val form = uiState as CadastroEventoUiState.Form
                val isFormValid =
                    form.titulo.isNotBlank() &&
                            form.descricao.isNotBlank() &&
                            form.local.isNotBlank() &&
                            form.data.text.length == 10 &&
                            form.horario.text.length == 5

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CampoTextoForm(
                            label = "Título do evento",
                            valor = form.titulo,
                            aoMudar = { newValue ->
                                viewModel.updateTitulo(newValue)
                            },
                            icone = Icons.Default.Title,
                            erro = form.tituloError
                        )

                        OutlinedTextField(
                            value = form.descricao,
                            onValueChange = viewModel::updateDescricao,
                            label = { Text("Descrição") },
                            leadingIcon = {
                                Icon(Icons.Default.Event, contentDescription = null)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            maxLines = 5
                        )

                        CampoTextoForm(
                            label = "Local do evento",
                            valor = form.local,
                            aoMudar = { newValue ->
                                viewModel.updateLocal(newValue)
                            },
                            icone = Icons.Default.LocationOn,
                            erro = form.localError
                        )

                        CampoTextoComMascara(
                            label = "Data (dd/MM/yyyy)",
                            valor = form.data,
                            aoMudar = { newValue ->
                                val maskedValue = applyDateMask(newValue)
                                viewModel.updateData(maskedValue)
                            },
                            icone = Icons.Default.CalendarToday,
                            erro = form.dataError
                        )

                        CampoTextoComMascara(
                            label = "Horário (HH:mm)",
                            valor = form.horario,
                            aoMudar = { newValue ->
                                val maskedValue = applySimpleTimeMask(newValue)
                                viewModel.updateHorario(maskedValue)
                            },
                            icone = Icons.Default.Schedule,
                            erro = form.horarioError
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TextButton(
                                onClick = onNavigateBack,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar")
                            }

                            Button(
                                onClick = { showCriarEditarDialog.value = true },
                                modifier = Modifier.weight(1f),
                                enabled = !form.isLoading && isFormValid
                            ) {
                                Text(if (eventoId.isNullOrBlank()) "Criar Evento" else "Salvar Alterações")
                            }
                        }
                    }
                }
            }

            if (showCriarEditarDialog.value) {
                AlertDialog(
                    onDismissRequest = { showCriarEditarDialog.value = false },
                    title = {
                        Text(
                            text = if (eventoId.isNullOrBlank()) "Confirmar Criação" else "Confirmar Alterações",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    text = {
                        Text(
                            text = if (eventoId.isNullOrBlank())
                                "Deseja criar este novo evento?"
                            else
                                "Deseja salvar as alterações no evento?",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.salvarEvento(
                                    onSuccess = {
                                        showCriarEditarDialog.value = false
                                        onNavigateBack()
                                    },
                                )
                            }
                        ) {
                            Text(if (eventoId.isNullOrBlank()) "Criar" else "Salvar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showCriarEditarDialog.value = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

        }
    }
}

@Composable
private fun CampoTextoComMascara(
    label: String,
    valor: TextFieldValue,
    aoMudar: (TextFieldValue) -> Unit,
    icone: ImageVector,
    erro: String?
) {
    Column {
        OutlinedTextField(
            value = valor,
            onValueChange = aoMudar,
            label = { Text(label) },
            leadingIcon = { Icon(icone, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = erro != null
        )
        erro?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
private fun CampoTextoForm(
    label: String,
    valor: String,
    aoMudar: (String) -> Unit,
    icone: ImageVector,
    erro: String?
) {
    Column {
        OutlinedTextField(
            value = valor,
            onValueChange = aoMudar,
            label = { Text(label) },
            leadingIcon = { Icon(icone, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = erro != null
        )
        erro?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

