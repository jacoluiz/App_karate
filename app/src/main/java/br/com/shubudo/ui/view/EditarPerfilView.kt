package br.com.shubudo.ui.view

import android.util.Log
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import br.com.shubudo.ui.components.CustomIconButton
import br.com.shubudo.ui.uistate.EditarPerfilUiState
import br.com.shubudo.ui.viewModel.EditarPerfilViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.Date
import java.util.Locale

data class DateValidation(
    val isValid: Boolean,
    val message: String,
    val age: Int? = null
)

// Função para aplicar máscara de altura no formato x,xx
private fun applyHeightMask(input: String, previousValue: String = ""): Pair<String, Int> {
    // Remove qualquer caractere que não seja dígito ou vírgula
    val digitsOnly = input.filter { it.isDigit() || it == ',' }

    // Se estiver vazio, retorna vazio
    if (digitsOnly.isEmpty()) return Pair("", 0)

    // Remove vírgulas duplicadas ou no início
    val cleanInput = digitsOnly.replace(Regex(",+"), ",").removePrefix(",")

    // Se só tem vírgula, retorna vazio
    if (cleanInput == ",") return Pair("", 0)

    // Separa por vírgula
    val parts = cleanInput.split(",")

    val result = when {
        // Se não tem vírgula e tem 1 dígito
        parts.size == 1 && parts[0].length <= 1 -> parts[0]

        // Se não tem vírgula e tem mais de 1 dígito, adiciona vírgula automaticamente
        parts.size == 1 && parts[0].length > 1 -> {
            val digits = parts[0]
            if (digits.length >= 3) {
                // Limita a 3 dígitos e formata como x,xx
                "${digits[0]},${digits.substring(1, 3)}"
            } else {
                "${digits[0]},${digits.substring(1)}"
            }
        }

        // Se já tem vírgula
        parts.size == 2 -> {
            val beforeComma = parts[0].take(1) // Máximo 1 dígito antes da vírgula
            val afterComma = parts[1].take(2)  // Máximo 2 dígitos depois da vírgula

            if (beforeComma.isEmpty()) {
                if (afterComma.isEmpty()) "" else ",${afterComma}"
            } else {
                "${beforeComma},${afterComma}"
            }
        }

        // Casos com múltiplas vírgulas - pega apenas a primeira parte válida
        else -> {
            val firstPart = parts[0].take(1)
            val secondPart = if (parts.size > 1) parts[1].take(2) else ""
            if (firstPart.isEmpty()) secondPart else "${firstPart},${secondPart}"
        }
    }

    // Calcula a nova posição do cursor
    val cursorPosition = when {
        // Se a vírgula foi inserida automaticamente, posiciona o cursor no final
        result.length > previousValue.length && result.contains(",") && !previousValue.contains(",") -> {
            result.length
        }
        // Caso contrário, posiciona no final
        else -> result.length
    }

    return Pair(result, cursorPosition)
}

// Função para validar altura
private fun validateHeight(height: String): Boolean {
    if (height.isEmpty()) return true // Campo vazio é válido (opcional)

    // Regex para validar formato x,xx onde x são dígitos
    val heightRegex = Regex("^[0-2]$|^[0-2],[0-9]{1,2}$")

    if (!heightRegex.matches(height)) return false

    // Converte para float para validação adicional
    val heightValue = height.replace(",", ".").toFloatOrNull() ?: return false

    // Valida range realístico (0.5m a 2.5m)
    return heightValue in 0.5f..2.5f
}

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
            initialOffsetY = { it / 4 },
            animationSpec = tween(600, easing = EaseOutCubic)
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
                EditarPerfilContent(
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
                    tamanhoFaixa = uiState.tamanhoFaixa
                )
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
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "loading")
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "rotation"
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
            ),
        contentAlignment = Alignment.Center
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
    themeViewModel: ThemeViewModel,
    onSave: () -> Unit,
    onCancelar: () -> Unit
) {
    // Estados locais para cada campo
    var currentNome by remember { mutableStateOf(nome) }
    var currentUsername by remember { mutableStateOf(username) }
    var currentEmail by remember { mutableStateOf(email) }
    var currentDataNascimento by remember { mutableStateOf(dataNascimento) }
    var currentPeso by remember { mutableStateOf(peso) }
    var currentAltura by remember { mutableStateOf(altura) }
    var currentFaixa by remember { mutableStateOf(corFaixa) }
    var currentDan by remember { mutableStateOf(dan) }
    var currentAcademia by remember { mutableStateOf(academia) }
    var currentTamanhoFaixa by remember { mutableStateOf(tamanhoFaixa) }

    // Controle do diálogo para selecionar faixa
    var showFaixaDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showDanDialog by remember { mutableStateOf(false) }
    var showAcademiaDialog by remember { mutableStateOf(false) }
    var showTamanhoFaixaDialog by remember { mutableStateOf(false) }

    val faixas = listOf("Branca", "Amarela", "Laranja", "Verde", "Roxa", "Marrom", "Preta", "Mestre", "Grão Mestre")
    val academias = listOf("CDL Team Boa Vista", "CDL Team Av. Das Torres", "Outros")
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
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(
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
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
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
                        onDatePickerClick = { showDatePicker = true }
                    )

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
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next
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
                        onClick = { showFaixaDialog = true }
                    )

                    // Dan - só mostra para faixas Preta, Mestre ou Grão Mestre
                    if (shouldShowDan(currentFaixa)) {
                        DanSelectionCard(
                            currentDan = currentDan,
                            corFaixa = currentFaixa,
                            onClick = { showDanDialog = true }
                        )
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

                    // Academia Selection
                    AcademiaSelectionCard(
                        currentAcademia = currentAcademia,
                        onClick = { showAcademiaDialog = true }
                    )

                    // Campo para "Outros" academia
                    if (currentAcademia == "Outros") {
                        AnimatedTextField(
                            value = currentAcademia,
                            onValueChange = { currentAcademia = it },
                            label = "Nome da Academia",
                            icon = Icons.Default.Edit,
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                        )
                    }

                    // Tamanho da Faixa
                    TamanhoFaixaSelectionCard(
                        currentTamanhoFaixa = currentTamanhoFaixa,
                        onClick = { showTamanhoFaixaDialog = true }
                    )
                }
            }

            // Action Buttons
            ActionButtons(
                isSaving = isSaving,
                isFormValid = currentNome.isNotBlank(), // Remove a validação rígida da data para permitir salvar com idade antiga
                onSave = {
                    if (!isSaving) {
                        isSaving = true
                        coroutineScope.launch {
                            editarPerfilViewModel.salvarPerfil(
                                nome = currentNome,
                                username = currentUsername,
                                email = currentEmail,
                                corFaixa = currentFaixa,
                                idade = currentDataNascimento,
                                peso = currentPeso,
                                altura = currentAltura,
                                senha = "",
                                dan = currentDan,
                                academia = currentAcademia,
                                tamanhoFaixa = currentTamanhoFaixa
                            )
                            delay(2000L)
                            onSave()
                            isSaving = false
                        }
                    }
                },
                onCancelar = onCancelar
            )
        }

        // Belt Selection Dialog
        if (showFaixaDialog) {
            BeltSelectionDialog(
                faixas = faixas,
                currentFaixa = currentFaixa,
                onFaixaSelected = { faixa ->
                    currentFaixa = faixa
                    // Reset dan quando muda a faixa
                    currentDan = 0
                    themeViewModel.changeThemeFaixa(faixa)
                    showFaixaDialog = false
                },
                onDismiss = { showFaixaDialog = false }
            )
        }

        // Date Picker Dialog
        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { selectedDate ->
                    currentDataNascimento = selectedDate
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
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
                onDismiss = { showDanDialog = false }
            )
        }

        // Academia Selection Dialog
        if (showAcademiaDialog) {
            AcademiaSelectionDialog(
                academias = academias,
                currentAcademia = currentAcademia,
                onAcademiaSelected = { academia ->
                    currentAcademia = academia
                    showAcademiaDialog = false
                },
                onDismiss = { showAcademiaDialog = false }
            )
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
                onDismiss = { showTamanhoFaixaDialog = false }
            )
        }
    }
}

@Composable
private fun ProfileHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
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
                ),
            contentAlignment = Alignment.Center
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
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
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeightTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(value)) }
    val isValid = validateHeight(value)

    val animatedBorderColor by animateColorAsState(
        targetValue = when {
            !isValid && value.isNotEmpty() -> MaterialTheme.colorScheme.error
            isFocused -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        },
        animationSpec = tween(200),
        label = "border_color"
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
                val (maskedValue, cursorPosition) = applyHeightMask(newTextFieldValue.text, textFieldValue.text)

                // Atualiza o estado local com a nova posição do cursor
                textFieldValue = newTextFieldValue.copy(
                    text = maskedValue,
                    selection = TextRange(cursorPosition)
                )

                // Notifica a mudança para o componente pai
                onValueChange(maskedValue)
            },
            label = { Text("Altura (m)") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Height,
                    contentDescription = null,
                    tint = labelColor
                )
            },
            placeholder = {
                Text(
                    text = "1,75",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
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
    value: String,
    validation: DateValidation,
    onDatePickerClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    // Verifica se é um valor antigo (apenas número)
    val isOldFormat = value.matches(Regex("^\\d{1,3}$"))
    val displayValue = if (isOldFormat) "" else formatDateForDisplay(value)
    val placeholderText = if (isOldFormat) "Toque para selecionar data de nascimento" else "Selecione sua data de nascimento"
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
                .clickable { onDatePickerClick() }
        ) {
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
    currentFaixa: String,
    onClick: () -> Unit
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
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
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
private fun ActionButtons(
    isSaving: Boolean,
    isFormValid: Boolean,
    onSave: () -> Unit,
    onCancelar: () -> Unit
) {
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
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Cancelar",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Button(
            onClick = onSave,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = !isSaving && isFormValid
        ) {
            AnimatedContent(
                targetState = isSaving,
                transitionSpec = {
                    fadeIn(animationSpec = tween(200)) togetherWith fadeOut(
                        animationSpec = tween(200)
                    )
                },
                label = "button_content"
            ) { saving ->
                if (saving) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "Salvando...",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Salvar",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
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
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Selecione sua Faixa",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected)
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            else MaterialTheme.colorScheme.surface
                        ),
                        border = if (isSelected) BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        ) else null
                    ) {
                        CustomIconButton(
                            texto = faixa,
                            iconPainter = iconPainter,
                            onClick = { onFaixaSelected(faixa) },
                            cor = selecionaCorIcone(faixa, isSystemInDarkTheme())
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(20.dp),
        properties = DialogProperties()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerModal(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = null,
        yearRange = 1900..2099
    )

    val confirmEnabled = remember(datePickerState.selectedDateMillis) {
        derivedStateOf {
            datePickerState.selectedDateMillis != null
        }
    }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Date(millis)
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
                        onDateSelected(formatter.format(date))
                    }
                },
                enabled = confirmEnabled.value
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = "Selecione sua data de nascimento",
                    modifier = Modifier.padding(16.dp)
                )
            },
        )
    }
}

@Composable
private fun DanSelectionCard(
    currentDan: Int,
    corFaixa: String,
    onClick: () -> Unit
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
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
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
private fun AcademiaSection(
    academia: String,
    onAcademiaChange: (String) -> Unit,
    onShowDialog: () -> Unit
) {
    val predefinedAcademias = listOf("CDL Team Boa Vista", "CDL Team Av. Das Torres")
    var showCustomField by remember(academia) {
        mutableStateOf(academia.isNotBlank() && !predefinedAcademias.contains(academia))
    }

    Column {
        Text(
            text = "Academia",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (showCustomField) {
            // Campo de texto personalizado
            OutlinedTextField(
                value = academia,
                onValueChange = onAcademiaChange,
                placeholder = {
                    Text(
                        "Digite o nome da sua academia",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sequencia_de_combate),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        showCustomField = false
                        onAcademiaChange("")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Voltar para seleção",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                ),
                singleLine = true
            )
        } else {
            // Card de seleção
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onShowDialog() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
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
                            painter = painterResource(id = R.drawable.ic_sequencia_de_combate),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = academia.ifBlank { "Selecionar Academia" },
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (academia.isBlank())
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AcademiaSelectionCard(
    currentAcademia: String,
    onClick: () -> Unit
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
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
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
                    painter = painterResource(id = R.drawable.ic_sequencia_de_combate),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Column {
                    Text(
                        text = "Academia",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = currentAcademia.ifBlank { "Selecionar Academia" },
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
    currentTamanhoFaixa: String,
    onClick: () -> Unit
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
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
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
    danOptions: List<Int>,
    currentDan: Int,
    onDanSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Selecione seu Dan",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(danOptions.size) { index ->
                    val dan = danOptions[index]
                    val isSelected = dan == currentDan
                    val danText = if (dan == 0) "Sem Dan" else "${dan}º Dan"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDanSelected(dan) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected)
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            else MaterialTheme.colorScheme.surface
                        ),
                        border = if (isSelected) BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        ) else null
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
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = danText,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(20.dp),
        properties = DialogProperties()
    )
}

@Composable
private fun AcademiaSelectionDialog(
    academias: List<String>,
    currentAcademia: String,
    onAcademiaSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var showCustomField by remember { mutableStateOf(false) }
    var customAcademia by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Selecione sua Academia",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            if (showCustomField) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = customAcademia,
                        onValueChange = { customAcademia = it },
                        label = { Text("Nome da Academia") },
                        placeholder = { Text("Digite o nome da sua academia") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(
                            onClick = {
                                showCustomField = false
                                customAcademia = ""
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Voltar")
                        }

                        Button(
                            onClick = {
                                if (customAcademia.isNotBlank()) {
                                    onAcademiaSelected(customAcademia)
                                }
                            },
                            enabled = customAcademia.isNotBlank(),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Confirmar")
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(academias.size) { index ->
                        val academia = academias[index]
                        val isSelected = academia == currentAcademia

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    Log.d("Tag", "Acdamia clicada $academia")
                                    if (academia == "Outros") {
                                        showCustomField = true
                                    } else {
                                        onAcademiaSelected(academia)
                                    }
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected)
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                else MaterialTheme.colorScheme.surface
                            ),
                            border = if (isSelected) BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            ) else null
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_sequencia_de_combate),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )

                                Text(
                                    text = academia,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            if (!showCustomField) {
                TextButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cancelar")
                }
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
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
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Selecione o Tamanho da Faixa",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tamanhos.size) { index ->
                    val tamanho = tamanhos[index]
                    val isSelected = tamanho == currentTamanho

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTamanhoSelected(tamanho) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected)
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            else MaterialTheme.colorScheme.surface
                        ),
                        border = if (isSelected) BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        ) else null
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
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = tamanho,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(20.dp),
        properties = DialogProperties()
    )
}

// Função para validar data de nascimento
private fun validateBirthDate(dateString: String): DateValidation {
    if (dateString.isEmpty()) {
        return DateValidation(false, "Data de nascimento é obrigatória")
    }

    // Se for apenas um número (idade antiga do banco), considera como válido mas pede para atualizar
    if (dateString.matches(Regex("^\\d{1,3}$"))) {
        val age = dateString.toIntOrNull()
        return if (age != null && age in 5..120) {
            DateValidation(true, "Idade: $age anos - Atualize para data completa", age)
        } else {
            DateValidation(false, "Idade inválida - Digite uma data de nascimento")
        }
    }

    return try {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        formatter.isLenient = false // Não permite datas inválidas como 31/02/2023
        val birthDate = formatter.parse(dateString) ?: return DateValidation(false, "Data inválida")

        val birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val today = LocalDate.now()

        // Verifica se a data não é futura
        if (birthLocalDate.isAfter(today)) {
            return DateValidation(false, "Data não pode ser futura")
        }

        // Calcula a idade
        val age = Period.between(birthLocalDate, today).years

        // Verifica limites de idade
        when {
            age < 5 -> DateValidation(false, "Idade mínima: 5 anos")
            age > 120 -> DateValidation(false, "Idade máxima: 120 anos")
            else -> DateValidation(true, "$age anos", age)
        }
    } catch (e: Exception) {
        DateValidation(false, "Formato de data inválido")
    }
}

// Função para verificar se deve mostrar o campo Dan
fun shouldShowDan(corFaixa: String): Boolean {
    return corFaixa in listOf("Preta", "Mestre", "Grão Mestre")
}

// Função para obter as opções de Dan baseado na faixa
fun getDanOptions(corFaixa: String): List<Int> {
    return when (corFaixa) {
        "Preta" -> (0..4).toList()
        "Mestre" -> (5..9).toList()
        "Grão Mestre" -> (10..12).toList()
        else -> emptyList()
    }
}

// Função para formatar data para exibição
private fun formatDateForDisplay(dateString: String): String {
    if (dateString.isEmpty()) return ""

    // Se for apenas um número (idade), retorna vazio para permitir edição
    if (dateString.matches(Regex("^\\d{1,3}$"))) {
        return ""
    }

    // Se já está no formato dd/MM/yyyy, retorna como está
    if (dateString.matches(Regex("^\\d{2}/\\d{2}/\\d{4}$"))) {
        return dateString
    }

    // Se está no formato ISO (yyyy-MM-dd), converte para dd/MM/yyyy
    if (dateString.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$"))) {
        return try {
            val inputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale("pt", "BR"))
            val outputFormatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
            val date = inputFormatter.parse(dateString)
            date?.let { outputFormatter.format(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }

    return dateString
}