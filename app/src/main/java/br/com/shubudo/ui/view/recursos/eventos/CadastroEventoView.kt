package br.com.shubudo.ui.view.recursos.eventos

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.ui.components.LoadingWrapper
import br.com.shubudo.ui.uistate.CadastroEventoUiState
import br.com.shubudo.ui.viewModel.CadastroEventoViewModel
import br.com.shubudo.utils.aplicarMascaraDeDataParaAniversario
import br.com.shubudo.utils.applySimpleTimeMask

@Composable
fun CadastroEventoView(
    eventoId: String? = null,
    onNavigateBack: () -> Unit = {},
    viewModel: CadastroEventoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCriarEditarDialog = remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

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
        ) {
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
                            text = if (eventoId.isNullOrBlank()) "Novo Evento" else "Editar Evento",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (eventoId.isNullOrBlank()) "Criar um novo evento" else "Editar informações do evento",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // Conteúdo principal
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
                        CampoDeTextoPadrao(
                            value = form.titulo,
                            onValueChange = { viewModel.updateTitulo(it) },
                            label = "Título do evento",
                            placeholder = "Digite o título do evento",
                            leadingIcon = Icons.Default.Title,
                            focusRequester = remember { FocusRequester() },
                            onFocusChanged = {},
                            singleLine = true,
                            maxLines = 1
                        )

                        CampoDeTextoPadrao(
                            value = form.descricao,
                            onValueChange = viewModel::updateDescricao,
                            label = "Descrição",
                            placeholder = "Digite a descrição do evento",
                            leadingIcon = Icons.Default.Event,
                            focusRequester = remember { FocusRequester() },
                            onFocusChanged = {},
                            singleLine = false,
                            maxLines = 5
                        )

                        CampoDeTextoPadrao(
                            value = form.local,
                            onValueChange = viewModel::updateLocal,
                            label = "Local do evento",
                            placeholder = "Digite o local",
                            leadingIcon = Icons.Default.LocationOn,
                            focusRequester = remember { FocusRequester() },
                            onFocusChanged = {},
                            singleLine = true,
                            maxLines = 1
                        )

                        CampoDeTextoComMascaraPadrao(
                            value = form.data,
                            onValueChange = {
                                val masked = aplicarMascaraDeDataParaAniversario(it)
                                viewModel.updateData(masked)
                            },
                            label = "Data (dd/MM/yyyy)",
                            placeholder = "dd/MM/yyyy",
                            leadingIcon = Icons.Default.CalendarToday,
                            focusRequester = remember { FocusRequester() },
                            onFocusChanged = {},
                            isError = form.dataError != null
                        )

                        CampoDeTextoComMascaraPadrao(
                            value = form.horario,
                            onValueChange = {
                                val masked = applySimpleTimeMask(it)
                                viewModel.updateHorario(masked)
                            },
                            label = "Horário (HH:mm)",
                            placeholder = "HH:mm",
                            leadingIcon = Icons.Default.Schedule,
                            focusRequester = remember { FocusRequester() },
                            onFocusChanged = {},
                            isError = form.horarioError != null
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
                        LoadingButton(
                            onClick = {
                                isSaving = true
                                viewModel.salvarEvento(
                                    onSuccess = {
                                        isSaving = false
                                        showCriarEditarDialog.value = false
                                        onNavigateBack()
                                    },
                                    onError = {
                                        isSaving = false
                                    }
                                )
                            },
                            isLoading = isSaving,
                            enabled = true,
                            text = if (eventoId.isNullOrBlank()) "Criar" else "Salvar",
                            loadingText = if (eventoId.isNullOrBlank()) "Criando..." else "Salvando...",
                            modifier = Modifier.height(48.dp) // altura menor para dialog
                        )
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
fun CampoDeTextoComMascaraPadrao(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    focusRequester: FocusRequester,
    onFocusChanged: ((Boolean) -> Unit)? = null,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { onFocusChanged?.invoke(it.isFocused) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            isError = isError
        )

        if (isError) {
            Text(
                text = "Formato inválido",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
