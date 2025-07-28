package br.com.shubudo.ui.view

import LoadingButton
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.R
import br.com.shubudo.SessionManager.usuarioLogado
import br.com.shubudo.ui.components.AcademiaSelector
import br.com.shubudo.ui.uistate.EditarPerfilUiState
import br.com.shubudo.ui.viewModel.EditarPerfilViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel
import br.com.shubudo.utils.DateValidation
import br.com.shubudo.utils.applyHeightMask
import br.com.shubudo.utils.formatDateForDisplay
import br.com.shubudo.utils.getCorDaFaixa
import br.com.shubudo.utils.getDanOptions
import br.com.shubudo.utils.shouldShowDan
import br.com.shubudo.utils.validateBirthDate
import br.com.shubudo.utils.validateHeight
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EditarPerfilView(
    uiState: EditarPerfilUiState,
    editarPerfilViewModel: EditarPerfilViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel,
    onSave: () -> Unit,
    onCancelar: () -> Unit
) {
    val animatedVisibility by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = animatedVisibility,
        enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
            initialOffsetY = { it / 4 }, animationSpec = tween(600, easing = EaseOutCubic)
        )
    ) {
        when (uiState) {
            is EditarPerfilUiState.Loading -> {
                LoadingState()
            }

            is EditarPerfilUiState.Empty -> {
                EmptyState()
            }

            is EditarPerfilUiState.Success -> {
                val lesoes = uiState.lesaoOuLaudosMedicos
                val registro = uiState.registroAKSD

                if (lesoes != null && registro != null) {
                    EditarPerfilContent(
                        id = uiState.id,
                        nome = uiState.nome,
                        username = uiState.username,
                        email = uiState.email,
                        dataNascimento = uiState.idade,
                        peso = uiState.peso,
                        altura = uiState.altura,
                        corFaixa = uiState.corFaixa,
                        editarPerfilViewModel = editarPerfilViewModel,
                        themeViewModel = themeViewModel,
                        onSave = onSave,
                        onCancelar = onCancelar,
                        dan = uiState.dan,
                        academia = uiState.academia,
                        tamanhoFaixa = uiState.tamanhoFaixa,
                        lesoesOuLaudosMedicos = lesoes,
                        status = uiState.status,
                        registroAKSD = registro,
                        perfis = uiState.perfis,
                    )
                }
            }

        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                )
            ), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "loading")
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing), repeatMode = RepeatMode.Restart
                ), label = "rotation"
            )

            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .graphicsLayer { rotationZ = rotation },
                strokeWidth = 4.dp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Carregando perfil...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                )
            ), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nenhum dado de perfil disponível",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EditarPerfilContent(
    id: String,
    nome: String,
    username: String,
    email: String,
    dataNascimento: String,
    peso: String,
    altura: String,
    corFaixa: String,
    dan: Int,
    academia: String,
    tamanhoFaixa: String,
    editarPerfilViewModel: EditarPerfilViewModel,
    lesoesOuLaudosMedicos: String,
    registroAKSD: String,
    perfis: List<String>,
    status: String = "ativo",
    themeViewModel: ThemeViewModel,
    onSave: () -> Unit,
    onCancelar: () -> Unit
) {
    // Estados locais para cada campo
    var currentId by remember { mutableStateOf(id) }
    var currentNome by remember { mutableStateOf(nome) }
    var currentUsername by remember { mutableStateOf(username) }
    var currentEmail by remember { mutableStateOf(email) }
    var currentDataNascimento by remember { mutableStateOf(dataNascimento) }
    var currentPeso by remember { mutableStateOf(peso) }
    var currentAltura by remember { mutableStateOf(altura) }
    var currentFaixa by remember { mutableStateOf(corFaixa) }
    var currentDan by remember { mutableIntStateOf(dan) }
    var currentAcademia by remember { mutableStateOf(academia) }
    var currentTamanhoFaixa by remember { mutableStateOf(tamanhoFaixa) }
    var currentLesaoOuLaudosMedicos by remember { mutableStateOf(lesoesOuLaudosMedicos) }
    var currentRegistroAKSD by remember { mutableStateOf(registroAKSD) }
    var currentPerfis by remember { mutableStateOf(perfis) }
    var currentStatus by remember { mutableStateOf(status) }

    val isFormValid = currentNome.isNotBlank() &&
            currentUsername.isNotBlank() &&
            currentEmail.isNotBlank() &&
            currentDataNascimento.isNotBlank() &&
            currentPeso.isNotBlank() &&
            currentAltura.isNotBlank() &&
            currentFaixa.isNotBlank() &&
            currentAcademia.isNotBlank() &&
            currentTamanhoFaixa.isNotBlank()


    val context = LocalContext.current

    // Controle do diálogo para selecionar faixa
    var showFaixaDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showDanDialog by remember { mutableStateOf(false) }
    var showAcademiaDialog by remember { mutableStateOf(false) }
    var showTamanhoFaixaDialog by remember { mutableStateOf(false) }
    var showPerfisDialog by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }


    val faixas = listOf(
        "Branca", "Amarela", "Laranja", "Verde", "Roxa", "Marrom", "Preta", "Mestre", "Grão Mestre"
    )
    val tamanhosFaixa = (1..8).map { "Tamanho $it" }

    // Estado para indicar se a operação de salvar está em andamento
    var isSaving by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Validação da data de nascimento
    val dateValidation = remember(currentDataNascimento) {
        validateBirthDate(currentDataNascimento)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            ProfileHeader()

            // Form Card
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ), shape = RoundedCornerShape(20.dp), colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "Informações Pessoais",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Personal Info Section
                    AnimatedTextField(
                        value = currentNome,
                        onValueChange = { currentNome = it },
                        label = "Nome Completo",
                        icon = Icons.Default.Person,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                    )

                    AnimatedTextField(
                        value = currentUsername,
                        onValueChange = { currentUsername = it },
                        label = "Nome de Usuário",
                        icon = Icons.Default.AccountCircle,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                    )

                    AnimatedTextField(
                        value = currentEmail,
                        onValueChange = { currentEmail = it },
                        label = "Email",
                        icon = Icons.Default.Email,
                        enabled = false,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                        )
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )

                    Text(
                        text = "Dados Pessoais",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Data de Nascimento Field
                    DateOfBirthField(
                        value = formatDateForDisplay(currentDataNascimento),
                        validation = dateValidation,
                        onDatePickerClick = { showDatePicker = true })

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AnimatedTextField(
                            value = currentPeso,
                            onValueChange = { currentPeso = it },
                            label = "Peso (kg)",
                            icon = Icons.Default.FitnessCenter,
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next
                            )
                        )

                        // Campo altura com máscara
                        HeightTextField(
                            value = currentAltura,
                            onValueChange = { currentAltura = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )

                    Text(
                        text = "Graduação",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Belt Selection
                    BeltSelectionCard(
                        currentFaixa = currentFaixa,
                        onClick = { showFaixaDialog = true })

                    // Dan - só mostra para faixas Preta, Mestre ou Grão Mestre
                    if (shouldShowDan(currentFaixa)) {
                        DanSelectionCard(
                            currentDan = currentDan,
                            onClick = { showDanDialog = true })
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )

                    Text(
                        text = "Academia e Equipamentos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Campo para Registro AKSD
                    AnimatedTextField(
                        value = currentRegistroAKSD,
                        onValueChange = { currentRegistroAKSD = it },
                        label = "Registro AKSD",
                        icon = Icons.Default.AccountCircle,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                        )
                    )
                    Text(
                        text = "Você não é obrigado a saber esse registro mas, no futuro, converse com o sensei para adiciona-lo aqui. Caso possua a carteirinha shubu-dô, nela contem esse número",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                    )

                    // Academia Selection
                    AcademiaSelector(
                        academia = currentAcademia,
                        onAcademiaChange = { currentAcademia = it })

                    // Tamanho da Faixa
                    TamanhoFaixaSelectionCard(
                        currentTamanhoFaixa = currentTamanhoFaixa,
                        onClick = { showTamanhoFaixaDialog = true })
                    Text(
                        text = "Você pode encontrar o número da faixa na etiqueta da faixa. Caso ache sua faixa muito grande fique a vontade para colocar um número menor",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )

                    Text(
                        text = "Informações Médicas (Opcional)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Campo para Lesões e Laudos Médicos
                    AnimatedTextField(
                        value = currentLesaoOuLaudosMedicos,
                        onValueChange = { currentLesaoOuLaudosMedicos = it },
                        label = "Lesões e Laudos Médicos",
                        icon = Icons.Default.Edit,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 5
                    )
                }
            }

            if (usuarioLogado?._id != currentId) {
                Spacer(modifier = Modifier.height(24.dp))
                // Seção administrativa
                Text(
                    text = "Configurações Administrativas",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Status Switch
                Card(
                    modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    ), shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Status do Usuário",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (currentStatus == "ativo") "Usuário ativo no sistema" else "Usuário inativo no sistema",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        Switch(checked = currentStatus == "ativo", onCheckedChange = { isChecked ->
                            currentStatus = if (isChecked) "ativo" else "inativo"
                        })
                    }
                }

                // Perfis
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showPerfisDialog = true },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Perfis do Usuário",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Toque para gerenciar os perfis",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = "Gerenciar perfis",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Mostrar perfis atuais
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            currentPerfis.forEach { perfil ->
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ), shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text(
                                        text = perfil,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp, vertical = 6.dp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onCancelar,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        width = 1.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cancelar", style = MaterialTheme.typography.titleMedium
                    )
                }

                LoadingButton(
                    onClick = {
                        if (!isSaving) {
                            isSaving = true
                            coroutineScope.launch {
                                editarPerfilViewModel.salvarPerfil(
                                    id = currentId,
                                    nome = currentNome,
                                    username = currentUsername,
                                    email = currentEmail,
                                    corFaixa = currentFaixa,
                                    idade = currentDataNascimento,
                                    peso = currentPeso,
                                    altura = currentAltura,
                                    dan = currentDan,
                                    academia = currentAcademia,
                                    tamanhoFaixa = currentTamanhoFaixa,
                                    lesaoOuLaudosMedicos = currentLesaoOuLaudosMedicos,
                                    registroAKSD = currentRegistroAKSD,
                                    perfis = currentPerfis,
                                    status = currentStatus,
                                    onSave = {
                                        isSaving = false
                                        onSave()
                                    },
                                    onError = {
                                        isSaving = false
                                        Toast.makeText(
                                            context,
                                            "Erro ao salvar",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    context = context
                                )
                            }
                        }
                    },
                    isLoading = isSaving,
                    enabled = isFormValid,
                    text = "Salvar",
                    loadingText = "Salvando...",
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                )
            }
        }

        // Dialog para seleção de perfis
        if (showPerfisDialog) {
            PerfisSelectionDialog(
                perfisAtuais = currentPerfis,
                onDismiss = { showPerfisDialog = false },
                onConfirm = { novosPerfis ->
                    currentPerfis = novosPerfis
                    showPerfisDialog = false
                })
        }

        // Belt Selection Dialog
        if (showFaixaDialog) {
            BeltSelectionDialog(
                faixas = faixas,
                currentFaixa = currentFaixa,
                onFaixaSelected = { faixa ->
                    currentFaixa = faixa
                    currentDan = when (faixa) {
                        "Mestre" -> 5
                        "Grão Mestre" -> 10
                        else -> 0
                    }
                    if (usuarioLogado?._id == currentId) {
                        themeViewModel.changeThemeFaixa(faixa)
                    }
                    showFaixaDialog = false
                },
                onDismiss = { showFaixaDialog = false })
        }

        // Date Picker Dialog
        if (showDatePicker) {
            DatePickerModal(onDateSelected = { selectedDate ->
                currentDataNascimento = selectedDate
                showDatePicker = false
            }, onDismiss = { showDatePicker = false })
        }

        // Dan Selection Dialog
        if (showDanDialog) {
            DanSelectionDialog(
                danOptions = getDanOptions(currentFaixa),
                currentDan = currentDan,
                onDanSelected = { dan ->
                    currentDan = dan
                    showDanDialog = false
                },
                onDismiss = { showDanDialog = false })
        }

        // Tamanho Faixa Selection Dialog
        if (showTamanhoFaixaDialog) {
            TamanhoFaixaSelectionDialog(
                tamanhos = tamanhosFaixa,
                currentTamanho = currentTamanhoFaixa,
                onTamanhoSelected = { tamanho ->
                    currentTamanhoFaixa = tamanho
                    showTamanhoFaixaDialog = false
                },
                onDismiss = { showTamanhoFaixaDialog = false })
        }
    }
}

@Composable
private fun ProfileHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        )
                    )
                ), contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Editar Perfil",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Atualize suas informações pessoais",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun AnimatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = 1
) {
    var isFocused by remember { mutableStateOf(false) }

    val animatedBorderColor by animateColorAsState(
        targetValue = if (!enabled) MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        else if (isFocused) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
        animationSpec = tween(200),
        label = "border_color"
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (!enabled) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                else if (isFocused) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        },
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused },
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = animatedBorderColor,
            unfocusedBorderColor = animatedBorderColor,
            disabledBorderColor = animatedBorderColor,
            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        ),
        maxLines = maxLines,
        minLines = if (maxLines > 1) 3 else 1
    )
}

@Composable
private fun HeightTextField(
    value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(value)) }
    val isValid = validateHeight(value)

    val animatedBorderColor by animateColorAsState(
        targetValue = when {
            !isValid && value.isNotEmpty() -> MaterialTheme.colorScheme.error
            isFocused -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        }, animationSpec = tween(200), label = "border_color"
    )

    val labelColor = when {
        !isValid && value.isNotEmpty() -> MaterialTheme.colorScheme.error
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }

    // Atualiza o TextFieldValue quando o value externo muda
    LaunchedEffect(value) {
        if (textFieldValue.text != value) {
            textFieldValue = textFieldValue.copy(text = value)
        }
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { newTextFieldValue ->
                val (maskedValue, cursorPosition) = applyHeightMask(
                    newTextFieldValue.text, textFieldValue.text
                )

                // Atualiza o estado local com a nova posição do cursor
                textFieldValue = newTextFieldValue.copy(
                    text = maskedValue, selection = TextRange(cursorPosition)
                )

                // Notifica a mudança para o componente pai
                onValueChange(maskedValue)
            },
            label = { Text("Altura (m)") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Height, contentDescription = null, tint = labelColor
                )
            },
            placeholder = {
                Text(
                    text = "1,75", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done
            ),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = animatedBorderColor,
                unfocusedBorderColor = animatedBorderColor,
                focusedLabelColor = labelColor,
                unfocusedLabelColor = labelColor,
                focusedLeadingIconColor = labelColor,
                unfocusedLeadingIconColor = labelColor
            ),
            isError = !isValid && value.isNotEmpty(),
            singleLine = true
        )

        // Mensagem de validação
        if (textFieldValue.text.isNotEmpty() && !isValid) {
            Text(
                text = "Formato: x,xx (ex: 1,75). Altura entre 0,5m e 2,5m",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
private fun DateOfBirthField(
    value: String, validation: DateValidation, onDatePickerClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    // Verifica se é um valor antigo (apenas número)
    val isOldFormat = value.matches(Regex("^\\d{1,3}$"))
    val displayValue = if (isOldFormat) "" else formatDateForDisplay(value)
    val placeholderText =
        if (isOldFormat) "Toque para selecionar data de nascimento" else "Selecione sua data de nascimento"
    val borderColor = when {
        !validation.isValid && value.isNotEmpty() && !isOldFormat -> MaterialTheme.colorScheme.error
        isOldFormat -> MaterialTheme.colorScheme.tertiary
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    }

    val labelColor = when {
        !validation.isValid && value.isNotEmpty() && !isOldFormat -> MaterialTheme.colorScheme.error
        isOldFormat -> MaterialTheme.colorScheme.tertiary
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDatePickerClick() }) {
            OutlinedTextField(
                value = displayValue,
                onValueChange = { },
                label = { Text("Data de Nascimento") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = labelColor
                    )
                },
                placeholder = {
                    Text(
                        text = placeholderText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                },
                readOnly = true,
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isFocused = it.isFocused },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = borderColor,
                    disabledLabelColor = labelColor,
                    disabledLeadingIconColor = labelColor,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface
                ),
                isError = !validation.isValid && value.isNotEmpty() && !isOldFormat
            )
        }

        // Validation message
        if (value.isNotEmpty()) {
            val messageColor = when {
                isOldFormat -> MaterialTheme.colorScheme.tertiary
                validation.isValid -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.error
            }

            Text(
                text = validation.message,
                style = MaterialTheme.typography.bodySmall,
                color = messageColor,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
private fun BeltSelectionCard(
    currentFaixa: String, onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        border = BorderStroke(
            width = 1.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_faixa),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = selecionaCorIcone(currentFaixa, isSystemInDarkTheme())
                )

                Column {
                    Text(
                        text = "Faixa Atual",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = currentFaixa,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun BeltSelectionDialog(
    faixas: List<String>,
    currentFaixa: String,
    onFaixaSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss, title = {
            Text(
                text = "Selecione sua Faixa",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }, text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(faixas.size) { index ->
                    val faixa = faixas[index]
                    val isSelected = faixa == currentFaixa
                    val iconPainter = if (faixa == "Branca" && !isSystemInDarkTheme()) {
                        painterResource(id = R.drawable.ic_faixa_outline)
                    } else {
                        painterResource(id = R.drawable.ic_faixa)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onFaixaSelected(faixa) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) getCorDaFaixa(faixa)
                            else MaterialTheme.colorScheme.surface
                        ),
                        border = if (!isSelected) BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        ) else null,
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isSelected) 4.dp else 1.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                painter = iconPainter,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else selecionaCorIcone(faixa, isSystemInDarkTheme())
                            )

                            Text(
                                text = faixa, style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                                ), color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selecionado",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                    }
                }
            }
        }, confirmButton = {}, dismissButton = {
            TextButton(
                onClick = onDismiss, shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancelar")
            }
        }, shape = RoundedCornerShape(20.dp), properties = DialogProperties()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerModal(
    onDateSelected: (String) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = null, yearRange = 1900..2099
    )

    val confirmEnabled = remember(datePickerState.selectedDateMillis) {
        derivedStateOf {
            datePickerState.selectedDateMillis != null
        }
    }

    DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(
            onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    val date = Date(millis)
                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
                    onDateSelected(formatter.format(date))
                }
            }, enabled = confirmEnabled.value
        ) {
            Text("Confirmar")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancelar")
        }
    }) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = "Selecione sua data de nascimento", modifier = Modifier.padding(16.dp)
                )
            },
        )
    }
}

@Composable
private fun DanSelectionCard(
    currentDan: Int, onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        border = BorderStroke(
            width = 1.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_faixa),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Column {
                    Text(
                        text = "Dan",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = when {
                            currentDan > 0 -> "${currentDan}º Dan"
                            currentDan == 0 -> "Sem Dan"
                            else -> "Selecionar Dan"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun TamanhoFaixaSelectionCard(
    currentTamanhoFaixa: String, onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        border = BorderStroke(
            width = 1.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_faixa),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Column {
                    Text(
                        text = "Tamanho da Faixa",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = currentTamanhoFaixa.ifBlank { "Selecionar Tamanho" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun DanSelectionDialog(
    danOptions: List<Int>, currentDan: Int, onDanSelected: (Int) -> Unit, onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss, title = {
            Text(
                text = "Selecione seu Dan",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }, text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(danOptions.size) { index ->
                    val dan = danOptions[index]
                    val isSelected = dan == currentDan
                    val danText = if (dan == 0) "Sem Dan" else "${dan}º Dan"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDanSelected(dan) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface
                        ),
                        border = if (!isSelected) BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        ) else null,
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isSelected) 4.dp else 1.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_faixa),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = danText, style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                                ), color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selecionado",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            }
        }, confirmButton = {}, dismissButton = {
            TextButton(
                onClick = onDismiss, shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancelar")
            }
        }, shape = RoundedCornerShape(20.dp), properties = DialogProperties()
    )
}

@Composable
private fun TamanhoFaixaSelectionDialog(
    tamanhos: List<String>,
    currentTamanho: String,
    onTamanhoSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss, title = {
            Text(
                text = "Selecione o Tamanho da Faixa",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }, text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tamanhos.size) { index ->
                    val tamanho = tamanhos[index]
                    val isSelected = tamanho == currentTamanho

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTamanhoSelected(tamanho) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface
                        ),
                        border = if (!isSelected) BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        ) else null,
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isSelected) 4.dp else 1.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_faixa),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = tamanho, style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                                ), color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selecionado",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            }
        }, confirmButton = {}, dismissButton = {
            TextButton(
                onClick = onDismiss, shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancelar")
            }
        }, shape = RoundedCornerShape(20.dp), properties = DialogProperties()
    )
}

@Composable
fun PerfisSelectionDialog(
    perfisAtuais: List<String>, onDismiss: () -> Unit, onConfirm: (List<String>) -> Unit
) {
    val perfisDisponiveis = listOf("adm", "professor", "aluno")
    var perfisTemporarios by remember { mutableStateOf(perfisAtuais.toSet()) }

    AlertDialog(onDismissRequest = onDismiss, title = {
        Text(
            text = "Selecionar Perfis",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }, text = {
        Column {
            Text(
                text = "Selecione os perfis que o usuário deve ter:",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                perfisDisponiveis.forEach { perfil ->
                    val isSelected = perfisTemporarios.contains(perfil)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                perfisTemporarios = if (perfisTemporarios.contains(perfil)) {
                                    perfisTemporarios - perfil
                                } else {
                                    perfisTemporarios + perfil
                                }
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface
                        ),
                        border = if (!isSelected) BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        ) else null,
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isSelected) 4.dp else 1.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = when (perfil) {
                                    "adm" -> Icons.Default.AccountCircle
                                    "professor" -> Icons.Default.Person
                                    "aluno" -> Icons.Default.Group
                                    else -> Icons.Default.Person
                                },
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = perfil.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                                ),
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selecionado",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }, confirmButton = {
        Button(
            onClick = { onConfirm(perfisTemporarios.toList()) },
            enabled = perfisTemporarios.isNotEmpty()
        ) {
            Text("Confirmar")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancelar")
        }
    })
}