package br.com.shubudo.ui.components

import ModernTextField
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.R
import br.com.shubudo.ui.viewModel.AcademiaSelectorViewModel

@Composable
fun AcademiaSelector(
    academia: String,
    onAcademiaChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
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
                    painter = painterResource(id = R.drawable.ic_academia),
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
                        text = academia.ifBlank { "Selecionar Academia" },
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

    if (showDialog) {
        AcademiaDialog(
            currentAcademia = academia,
            onAcademiaSelected = {
                onAcademiaChange(it)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun AcademiaDialog(
    currentAcademia: String,
    onAcademiaSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    viewModel: AcademiaSelectorViewModel = hiltViewModel()
) {
    val filiais = viewModel.filiais.value
    var searchText by remember { mutableStateOf("") }
    var showCustomField by remember { mutableStateOf(false) }
    var customAcademia by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val isLoading = viewModel.isLoading.value

    val filteredFiliais = filiais.filter {
        it.contains(searchText, ignoreCase = true)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecione sua Academia", style = MaterialTheme.typography.headlineSmall) },
        text = {
            when {
                isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Carregando academias...",
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }

                showCustomField -> {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ModernTextField(
                            value = customAcademia,
                            onValueChange = { customAcademia = it },
                            label = "Nome da Academia",
                            placeholder = "Digite o nome da sua academia",
                            leadingIcon = Icons.Default.Edit,
                            focusRequester = focusRequester
                        )

                        Row(Modifier.fillMaxWidth()) {
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
                }

                else -> {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ModernTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            label = "Buscar Academia",
                            placeholder = "Digite para buscar...",
                            leadingIcon = Icons.Default.Search,
                            focusRequester = focusRequester
                        )

                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(filteredFiliais.size) { index ->
                                val academia = filteredFiliais[index]
                                val isSelected = academia == currentAcademia

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (academia == "Outros") showCustomField = true
                                            else onAcademiaSelected(academia)
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected)
                                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                        else MaterialTheme.colorScheme.surface
                                    ),
                                    border = if (isSelected)
                                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                                    else null
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_academia),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                            text = academia,
                                            modifier = Modifier.padding(start = 12.dp),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            if (!showCustomField) {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}

