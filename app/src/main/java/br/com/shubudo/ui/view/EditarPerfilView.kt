package br.com.shubudo.ui.view

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.R
import br.com.shubudo.ui.components.CustomIconButton
import br.com.shubudo.ui.uistate.EditarPerfilUiState
import br.com.shubudo.ui.viewModel.EditarPerfilViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel

@Composable
fun EditarPerfilView(
    uiState: EditarPerfilUiState,
    editarPerfilViewModel: EditarPerfilViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel,
    onSave: () -> Boolean,
    onCancelar: () -> Unit
) {
    when (uiState) {
        is EditarPerfilUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is EditarPerfilUiState.Empty -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum dado de perfil disponível.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        is EditarPerfilUiState.Success -> {
            EditarPerfilContent(
                nome = uiState.nome,
                username = uiState.username,
                email = uiState.email,
                idade = uiState.idade,
                peso = uiState.peso,
                altura = uiState.altura,
                corFaixa = uiState.corFaixa,
                editarPerfilViewModel = editarPerfilViewModel,
                themeViewModel = themeViewModel,
                onSave = onSave,
                onCancelar = onCancelar
            )
        }
    }
}

@Composable
fun EditarPerfilContent(
    nome: String,
    username: String,
    email: String,
    idade: String,
    peso: String,
    altura: String,
    corFaixa: String,
    editarPerfilViewModel: EditarPerfilViewModel,
    themeViewModel: ThemeViewModel,
    onSave: () -> Boolean,
    onCancelar: () -> Unit
) {
    // Estados locais para cada campo
    var currentNome by remember { mutableStateOf(nome) }
    var currentUsername by remember { mutableStateOf(username) }
    var currentEmail by remember { mutableStateOf(email) }
    var currentIdade by remember { mutableStateOf(idade) }
    var currentPeso by remember { mutableStateOf(peso) }
    var currentAltura by remember { mutableStateOf(altura) }
    var currentFaixa by remember { mutableStateOf(corFaixa) }

    // Controla a visibilidade do AlertDialog
    var showFaixaDialog by remember { mutableStateOf(false) }

    // Lista de faixas (a mesma que você usava no DropDown)
    val faixas = listOf("Branca", "Amarela", "Laranja", "Verde", "Roxa", "Marrom", "Preta")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                // Campo Nome
                OutlinedTextField(
                    value = currentNome,
                    onValueChange = { currentNome = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                Spacer(Modifier.height(16.dp))

                // Campo Username
                OutlinedTextField(
                    value = currentUsername,
                    onValueChange = { currentUsername = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                Spacer(Modifier.height(16.dp))

                // Campo Email
                OutlinedTextField(
                    value = currentEmail,
                    onValueChange = { currentEmail = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(Modifier.height(16.dp))

                // Campo Idade
                OutlinedTextField(
                    value = currentIdade,
                    onValueChange = { currentIdade = it },
                    label = { Text("Idade") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(Modifier.height(16.dp))

                // Campo Peso
                OutlinedTextField(
                    value = currentPeso,
                    onValueChange = { currentPeso = it },
                    label = { Text("Peso") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(Modifier.height(16.dp))

                // Campo Altura
                OutlinedTextField(
                    value = currentAltura,
                    onValueChange = { currentAltura = it },
                    label = { Text("Altura") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(Modifier.height(16.dp))

                // Botão que abre o AlertDialog de faixas
                OutlinedButton (
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showFaixaDialog = true }
                ) {
                    Text(
                        text = "Faixa: $currentFaixa",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // ALERT DIALOG para seleção de faixa
                if (showFaixaDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showFaixaDialog = false
                        },
                        title = {
                            Text("Selecione a Faixa")
                        },
                        text = {
                            Column {
                                faixas.forEach { faixa ->
                                    val iconPainter =
                                        if (faixa == "Branca" && !isSystemInDarkTheme()) {
                                            painterResource(id = R.drawable.ic_faixa_outline)
                                        } else {
                                            painterResource(id = R.drawable.ic_faixa)
                                        }

                                    // Cada faixa será representada por um CustomIconButton
                                    CustomIconButton(
                                        texto = faixa,
                                        iconPainter = iconPainter,
                                        onClick = {
                                            currentFaixa = faixa
                                            themeViewModel.changeThemeFaixa(faixa)
                                            showFaixaDialog = false
                                        },
                                        cor = selecionaCorIcone(faixa, isSystemInDarkTheme())
                                    )
                                }
                            }
                        },
                        confirmButton = {
                            // Se quiser um botão "OK" além de selecionar direto, pode adicionar aqui
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showFaixaDialog = false }
                            ) {
                                Text("Cancelar")
                            }
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))

                // Botões de ação
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Botão Cancelar
                    Button(onClick = {
                        onCancelar()
                    }) {
                        Text("Cancelar")
                    }

                    // Botão Salvar
                    Button(
                        onClick = {
                            editarPerfilViewModel.salvarPerfil(
                                nome = currentNome,
                                username = currentUsername,
                                email = currentEmail,
                                corFaixa = currentFaixa,
                                idade = currentIdade,
                                peso = currentPeso,
                                altura = currentAltura,
                                senha = "" // Se quiser usar um campo de senha, adicione outro TextField
                            )
                            onSave()
                        }
                    ) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}
