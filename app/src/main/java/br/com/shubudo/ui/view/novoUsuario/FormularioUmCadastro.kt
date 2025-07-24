package br.com.shubudo.ui.view.novoUsuario

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import br.com.shubudo.R
import br.com.shubudo.ui.components.PasswordRequirement
import br.com.shubudo.ui.theme.PrimaryColorAmarela
import br.com.shubudo.ui.theme.PrimaryColorBranca
import br.com.shubudo.ui.theme.PrimaryColorGraoMestre
import br.com.shubudo.ui.theme.PrimaryColorLaranja
import br.com.shubudo.ui.theme.PrimaryColorMarron
import br.com.shubudo.ui.theme.PrimaryColorMestre
import br.com.shubudo.ui.theme.PrimaryColorPreta
import br.com.shubudo.ui.theme.PrimaryColorRoxa
import br.com.shubudo.ui.theme.PrimaryColorVerde
import br.com.shubudo.ui.viewModel.NovoUsuarioViewModel
import br.com.shubudo.utils.applyDateMask
import br.com.shubudo.utils.getDanOptions
import br.com.shubudo.utils.isValidDate
import br.com.shubudo.utils.senhaAtendeAosRequisitos
import br.com.shubudo.utils.validarRequisitosSenha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaginaUmCadastro(
    novoUsuarioViewModel: NovoUsuarioViewModel,
    scrollState: androidx.compose.foundation.ScrollState
) {
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    val requisitos = validarRequisitosSenha(novoUsuarioViewModel.senha)
    val mostrarRequisitos = novoUsuarioViewModel.senha.isNotBlank() && !senhaAtendeAosRequisitos(novoUsuarioViewModel.senha)

    var passwordVisible by remember { mutableStateOf(false) }
    var passwordVisibleConfirmSenha by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }
    var showBeltDropdown by remember { mutableStateOf(false) }
    var showDanDialog by remember { mutableStateOf(false) }
    var showAcademiaDialog by remember { mutableStateOf(false) }
    var dateValue by remember {
        mutableStateOf(TextFieldValue(novoUsuarioViewModel.idade))
    }

    val faixas = listOf(
        "Branca",
        "Amarela",
        "Laranja",
        "Verde",
        "Roxa",
        "Marrom",
        "Preta",
        "Mestre",
        "Grão Mestre"
    )
    val academias = listOf("CDL Team Boa Vista", "CDL Team Av. Das Torres", "Outros")

    val focusRequesterNome = remember { FocusRequester() }
    val focusRequesterIdade = remember { FocusRequester() }
    val focusRequesterAcademia = remember { FocusRequester() }
    val focusRequesterSenha = remember { FocusRequester() }
    val focusRequesterConfirmarSenha = remember { FocusRequester() }
    var hasTypedConfirmPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Belt Selection
        Column {
            Text(
                text = "Faixa",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ExposedDropdownMenuBox(
                expanded = showBeltDropdown,
                onExpandedChange = { showBeltDropdown = it }
            ) {
                OutlinedTextField(
                    value = novoUsuarioViewModel.faixa,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text(
                            "Selecione sua faixa",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_faixa),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = if (showBeltDropdown) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                )

                ExposedDropdownMenu(
                    expanded = showBeltDropdown,
                    onDismissRequest = { showBeltDropdown = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    faixas.forEach { faixa ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_faixa),
                                        contentDescription = null,
                                        tint = getBeltColor(faixa),
                                        modifier = Modifier.size(20.dp)
                                    )

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(getBeltColor(faixa).copy(alpha = 0.1f))
                                            .padding(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = faixa,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Medium
                                            ),
                                            color = getBeltColor(faixa)
                                        )
                                    }
                                }
                            },
                            onClick = {
                                novoUsuarioViewModel.faixa = faixa
                                showBeltDropdown = false
                                // Define Dan automático baseado na faixa
                                novoUsuarioViewModel.dan = when (faixa) {
                                    "Mestre" -> 5
                                    "Grão Mestre" -> 10
                                    else -> 0
                                }
                                // O tema será aplicado pelo LaunchedEffect no NovoUsuarioView
                            }
                        )
                    }
                }
            }
        }

        // Dan - só mostra para faixas Preta, Mestre ou Grão Mestre
        if (shouldShowDan(novoUsuarioViewModel.faixa)) {
            val danDisplayText = when (novoUsuarioViewModel.dan) {
                0 -> "Sem Dan"
                else -> "${novoUsuarioViewModel.dan}º Dan"
            }

            SelectionCard(
                label = "Dan",
                value = danDisplayText,
                icon = R.drawable.ic_faixa,
                onClick = { showDanDialog = true }
            )
        }

        // Name Field
        ModernTextField(
            value = novoUsuarioViewModel.nome,
            onValueChange = { novoUsuarioViewModel.nome = it },
            label = "Nome completo",
            placeholder = "Digite seu nome completo",
            leadingIcon = Icons.Default.Person,
            focusRequester = focusRequesterNome,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusRequesterIdade.requestFocus() }
            )
        )

        // Birth Date Field
        ModernTextFieldWithDateMask(
            value = dateValue,
            onValueChange = { newValue ->
                dateValue = applyDateMask(newValue)
                novoUsuarioViewModel.idade = dateValue.text
            },
            label = "Data de nascimento",
            placeholder = "dd/mm/aaaa",
            leadingIcon = Icons.Default.DateRange,
            focusRequester = focusRequesterIdade,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusRequesterAcademia.requestFocus() }
            ),
            helperText = "Formato: 25/12/1990",
            isError = dateValue.text.isNotEmpty() && !isValidDate(dateValue.text)
        )

        // Academia Selection
        AcademiaSection(
            academia = novoUsuarioViewModel.academia,
            onAcademiaChange = { novoUsuarioViewModel.academia = it },
            onShowDialog = { showAcademiaDialog = true }
        )

        // Password Field
        ModernTextField(
            value = novoUsuarioViewModel.senha,
            onValueChange = { novoUsuarioViewModel.senha = it },
            label = "Senha",
            placeholder = "Digite sua senha",
            leadingIcon = Icons.Default.Lock,
            trailingIcon = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
            onTrailingIconClick = { passwordVisible = !passwordVisible },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            focusRequester = focusRequesterSenha,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusRequesterConfirmarSenha.requestFocus() }
            ),
            onFocusChanged = { isFocused ->
                isPasswordFocused = isFocused
                if (isFocused) {
                    coroutineScope.launch {
                        kotlinx.coroutines.delay(500) // dá tempo do teclado abrir
                        scrollState.animateScrollTo(
                            with(density) { 1000.dp.toPx().toInt() }
                        )
                    }
                }
            }
        )

        // Password Requirements
        AnimatedVisibility(
            visible = mostrarRequisitos,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Requisitos da senha:",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    requisitos.forEach { (descricao, atendido) ->
                        PasswordRequirement(text = descricao, isMet = atendido)
                    }
                }
            }
        }

        // Confirm Password Field
        ModernTextField(
            value = novoUsuarioViewModel.confirmarSenha,
            onValueChange = { newValue ->
                novoUsuarioViewModel.confirmarSenha = newValue

                if (newValue.length == 1 && !hasTypedConfirmPassword) {
                    hasTypedConfirmPassword = true
                    coroutineScope.launch {
                        kotlinx.coroutines.delay(150) // espera o aviso aparecer
                        scrollState.animateScrollTo(
                            with(density) { 1000.dp.toPx().toInt() }
                        )
                    }
                }
            }
            ,
            label = "Confirmar senha",
            placeholder = "Confirme sua senha",
            leadingIcon = Icons.Default.Lock,
            trailingIcon = if (passwordVisibleConfirmSenha) Icons.Default.VisibilityOff else Icons.Default.Visibility,
            onTrailingIconClick = { passwordVisibleConfirmSenha = !passwordVisibleConfirmSenha },
            visualTransformation = if (passwordVisibleConfirmSenha) VisualTransformation.None else PasswordVisualTransformation(),
            focusRequester = focusRequesterConfirmarSenha,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            onFocusChanged = { isFocused ->
                if (isFocused) {
                    coroutineScope.launch {
                        // Scroll inicial quando o campo recebe foco
                        scrollState.animateScrollTo(
                            with(density) { 600.dp.toPx().toInt() }
                        )
                    }
                }
            }
        )

        // Confirm Password Validation
        AnimatedVisibility(
            visible = novoUsuarioViewModel.confirmarSenha.isNotEmpty(),
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = if (senhaAtendeAosRequisitos(novoUsuarioViewModel.confirmarSenha)) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = null,
                    tint = if (senhaAtendeAosRequisitos(novoUsuarioViewModel.confirmarSenha)) Color(0xFF059669) else Color(0xFFDC2626),
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = if (senhaAtendeAosRequisitos(novoUsuarioViewModel.confirmarSenha)) "Senhas conferem" else "As senhas não conferem",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (senhaAtendeAosRequisitos(novoUsuarioViewModel.confirmarSenha)) Color(0xFF059669) else Color(0xFFDC2626)
                )
            }
        }
    }

    // Dan Selection Dialog
    if (showDanDialog) {
        DanSelectionDialog(
            danOptions = getDanOptions(novoUsuarioViewModel.faixa),
            currentDan = novoUsuarioViewModel.dan,
            onDanSelected = { dan ->
                novoUsuarioViewModel.dan = dan
                showDanDialog = false
            },
            onDismiss = { showDanDialog = false }
        )
    }

    // Academia Selection Dialog
    if (showAcademiaDialog) {
        AcademiaSelectionDialog(
            academias = academias,
            currentAcademia = novoUsuarioViewModel.academia,
            onAcademiaSelected = { academia ->
                novoUsuarioViewModel.academia = academia
                showAcademiaDialog = false
            },
            onDismiss = { showAcademiaDialog = false }
        )
    }
}

@Composable
private fun AcademiaSection(
    academia: String,
    onAcademiaChange: (String) -> Unit,
    onShowDialog: () -> Unit
) {
    var showCustomField by remember {
        mutableStateOf(
            academia.isNotBlank() && !listOf(
                "CDL Team Boa Vista",
                "CDL Team Av. Das Torres"
            ).contains(academia)
        )
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

    // Atualiza o estado quando a academia muda externamente
    LaunchedEffect(academia) {
        showCustomField = academia.isNotBlank() && !listOf(
            "CDL Team Boa Vista",
            "CDL Team Av. Das Torres"
        ).contains(academia)
    }
}

@Composable
private fun SelectionCard(
    label: String,
    value: String,
    icon: Int,
    onClick: () -> Unit
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
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
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (value.contains("Selecionar"))
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

@Composable
fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    trailingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    focusRequester: FocusRequester,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onFocusChanged: ((Boolean) -> Unit)? = null
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    placeholder,
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
            trailingIcon = trailingIcon?.let { icon ->
                {
                    IconButton(onClick = { onTrailingIconClick?.invoke() }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            },
            visualTransformation = visualTransformation,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { onFocusChanged?.invoke(it.isFocused) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
    }
}

@Composable
private fun ModernTextFieldWithDateMask(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    focusRequester: FocusRequester,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    helperText: String? = null,
    isError: Boolean = false
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    placeholder,
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
            isError = isError,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                errorBorderColor = MaterialTheme.colorScheme.error
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )

        if (isError) {
            Text(
                text = "Data inválida. Verifique o dia, mês e ano.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        } else {
            helperText?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }
    }
}

private fun getBeltColor(belt: String): Color {
    return when (belt) {
        "Branca" -> PrimaryColorBranca
        "Amarela" -> PrimaryColorAmarela
        "Laranja" -> PrimaryColorLaranja
        "Verde" -> PrimaryColorVerde
        "Roxa" -> PrimaryColorRoxa
        "Marrom" -> PrimaryColorMarron
        "Preta" -> PrimaryColorPreta
        "Mestre" -> PrimaryColorMestre
        "Grão Mestre" -> PrimaryColorGraoMestre
        else -> Color(0xFF6366F1) // fallback
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
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
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
