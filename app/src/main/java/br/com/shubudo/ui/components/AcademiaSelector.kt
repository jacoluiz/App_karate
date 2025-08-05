package br.com.shubudo.ui.components

import CampoDeTextoPadrao
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
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
import br.com.shubudo.ui.viewModel.components.AcademiaSelectorViewModel

@Composable
fun AcademiaSelector(
    filialIdSelecionado: String?,
    onFilialSelecionada: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AcademiaSelectorViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    val nomeFilialSelecionada = viewModel.academias.value
        .flatMap { it.filiais }
        .find { it._id == filialIdSelecionado }
        ?.nome ?: "Selecionar Academia"

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
                        text = "Academia onde treina",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = nomeFilialSelecionada,
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
            filialSelecionada = filialIdSelecionado,
            onFilialSelecionada = {
                onFilialSelecionada(it)
                showDialog = false
            },
            onDismiss = { showDialog = false },
            viewModel = viewModel
        )
    }
}

@Composable
fun AcademiaDialog(
    filialSelecionada: String?,
    onFilialSelecionada: (String) -> Unit,
    onDismiss: () -> Unit,
    viewModel: AcademiaSelectorViewModel
) {
    var searchText by remember { mutableStateOf("") }
    val isLoading = viewModel.isLoading.value

    val academias = viewModel.academias.value
    val filiais = academias.flatMap { academia ->
        academia.filiais.map { filial -> filial to academia }
    }

    val filtered = filiais.filter { (filial, _) ->
        filial.nome.contains(searchText, ignoreCase = true)
    }


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecione sua Academia", style = MaterialTheme.typography.headlineSmall) },
        text = {
            if (isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text("Carregando academias...", modifier = Modifier.padding(top = 16.dp))
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    CampoDeTextoPadrao(
                        value = searchText,
                        onValueChange = { searchText = it },
                        label = "Buscar Academia",
                        placeholder = "Digite para buscar...",
                        leadingIcon = Icons.Default.Search,
                        focusRequester = remember { FocusRequester() }
                    )

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filtered.size) { index ->
                            val (filial, academiaMatriz) = filtered[index]

                            val isSelected = academiaMatriz._id == filialSelecionada

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        filial._id?.let {
                                            onFilialSelecionada(it)
                                        }
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
                                        text = filial.nome,
                                        modifier = Modifier.padding(start = 12.dp),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}