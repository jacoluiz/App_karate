package br.com.shubudo.ui.view.novoUsuario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.R
import br.com.shubudo.ui.viewModel.NovoUsuarioViewModel

fun applyShiftedMask(input: TextFieldValue): TextFieldValue {
    val digits = input.text.filter { it.isDigit() }
    val limitedDigits = digits.takeLast(3)

    val maskedText = when (limitedDigits.length) {
        1 -> "0,0${limitedDigits[0]}"
        2 -> "0,$limitedDigits"
        3 -> "${limitedDigits[0]},${limitedDigits.substring(1)}"
        else -> "0,00"
    }

    return TextFieldValue(maskedText, TextRange(maskedText.length))
}

@Composable
fun PaginaDoisCadastro(
    novoUsuarioViewModel: NovoUsuarioViewModel
) {
    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterAltura = remember { FocusRequester() }
    val focusRequesterPeso = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var alturaValue by remember {
        mutableStateOf(TextFieldValue(novoUsuarioViewModel.altura.ifBlank { "0,00" }))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header
        Column {
            Text(
                text = "Informações Físicas",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Complete seus dados para finalizar o cadastro",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Email Field
        ModernTextField(
            value = novoUsuarioViewModel.email,
            onValueChange = { novoUsuarioViewModel.email = it },
            label = "E-mail",
            placeholder = "email@exemplo.com",
            leadingIcon = Icons.Default.Email,
            focusRequester = focusRequesterEmail,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusRequesterAltura.requestFocus() }
            )
        )

        // Height Field
        ModernTextFieldWithMask(
            value = alturaValue,
            onValueChange = { newValue ->
                alturaValue = applyShiftedMask(newValue)
                novoUsuarioViewModel.altura = alturaValue.text
            },
            label = "Altura (cm)",
            placeholder = "0,00",
            leadingIcon = Icons.Default.Height,
            suffix = "cm",
            focusRequester = focusRequesterAltura,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusRequesterPeso.requestFocus() }
            ),
            helperText = "Formato: 1,75 para 175cm"
        )

        // Weight Field
        ModernTextField(
            value = novoUsuarioViewModel.peso,
            onValueChange = { value ->
                val filteredValue = value.filter { it.isDigit() }
                novoUsuarioViewModel.peso = filteredValue
            },
            label = "Peso (kg)",
            placeholder = "Ex: 72",
            leadingIcon = Icons.Default.FitnessCenter,
            suffix = "kg",
            focusRequester = focusRequesterPeso,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.clearFocus() }
            )
        )

        // Tamanho da Faixa
        var showTamanhoFaixaDialog by remember { mutableStateOf(false) }
        val tamanhosFaixa = (1..8).map { "Tamanho $it" }

        SelectionCard(
            label = "Tamanho da Faixa",
            value = novoUsuarioViewModel.tamanhoFaixa.ifBlank { "Selecionar Tamanho" },
            icon = R.drawable.ic_faixa,
            onClick = { showTamanhoFaixaDialog = true }
        )

        // Validation Message
        if (novoUsuarioViewModel.email.isBlank() ||
            novoUsuarioViewModel.altura.isBlank() ||
            novoUsuarioViewModel.altura == "0,00" ||
            novoUsuarioViewModel.peso.isBlank() ||
            novoUsuarioViewModel.tamanhoFaixa.isBlank()
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text = "⚠️ Preencha todos os campos para continuar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Tamanho Faixa Selection Dialog
        if (showTamanhoFaixaDialog) {
            TamanhoFaixaSelectionDialog(
                tamanhos = tamanhosFaixa,
                currentTamanho = novoUsuarioViewModel.tamanhoFaixa,
                onTamanhoSelected = { tamanho ->
                    novoUsuarioViewModel.tamanhoFaixa = tamanho
                    showTamanhoFaixaDialog = false
                },
                onDismiss = { showTamanhoFaixaDialog = false }
            )
        }
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

// Função para verificar se deve mostrar o campo Dan
fun shouldShowDan(corFaixa: String): Boolean {
    return corFaixa in listOf("Preta", "Mestre", "Grão Mestre")
}

@Composable
private fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    suffix: String? = null,
    focusRequester: FocusRequester,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    helperText: String? = null
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
            suffix = suffix?.let {
                {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )

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

@Composable
private fun ModernTextFieldWithMask(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    suffix: String? = null,
    focusRequester: FocusRequester,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    helperText: String? = null
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
            suffix = suffix?.let {
                {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )

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

@Composable
private fun SummaryItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value.ifBlank { "-" },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 4.dp),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
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
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    )
}