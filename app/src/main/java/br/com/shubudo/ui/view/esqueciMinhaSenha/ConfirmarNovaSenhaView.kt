package br.com.shubudo.ui.view.esqueciMinhaSenha

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.ui.uistate.EsqueciMinhaSenhaUiState
import br.com.shubudo.ui.viewModel.EsqueciMinhaSenhaViewModel

@Composable
fun ConfirmarNovaSenhaView(
    viewModel: EsqueciMinhaSenhaViewModel = hiltViewModel(),
    onVoltar: () -> Unit,
    onSenhaAlterada: () -> Unit
) {
    val uiState = viewModel.uiState.value
    val isLoading = uiState is EsqueciMinhaSenhaUiState.Loading
    val etapa by viewModel.etapa

    var mostrarNovaSenha by remember { mutableStateOf(false) }
    var mostrarConfirmarSenha by remember { mutableStateOf(false) }

    // Observa mudanças no estado para lidar com erros de código
    LaunchedEffect(uiState, etapa) {
        if (uiState is EsqueciMinhaSenhaUiState.Error) {
            val errorMessage = uiState.mensagem
            // Se o erro é relacionado ao código de verificação, voltar para etapa 2
            if (errorMessage.contains("verification code", ignoreCase = true) ||
                errorMessage.contains("CodeMismatchException", ignoreCase = true) ||
                errorMessage.contains("Invalid", ignoreCase = true) ||
                errorMessage.contains("código", ignoreCase = true)) {
                viewModel.setEtapa(2)
                viewModel.codigo = ""
            }
        } else if (uiState is EsqueciMinhaSenhaUiState.Success &&
            uiState.mensagem.contains("sucesso", ignoreCase = true)) {
            onSenhaAlterada()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.surface
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header com animação
                    AnimatedContent(
                        targetState = etapa,
                        transitionSpec = {
                            (slideInVertically { -it } + fadeIn()).togetherWith(slideOutVertically { it } + fadeOut())
                        },
                        label = "header_animation"
                    ) { currentEtapa ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (currentEtapa == 3) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Código Validado!",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Agora defina sua nova senha",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Verificação de Código",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Digite o código de verificação recebido por e-mail",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Campo do código (visível apenas quando não validado)
                    AnimatedVisibility(
                        visible = etapa == 2,
                        exit = slideOutVertically(
                            targetOffsetY = { -it },
                            animationSpec = tween(500)
                        ) + fadeOut(animationSpec = tween(300))
                    ) {
                        Column {
                            OutlinedTextField(
                                value = viewModel.codigo,
                                onValueChange = {
                                    if (it.length <= 6) viewModel.codigo = it
                                },
                                label = { Text("Código de Verificação") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(
                                        alpha = 0.5f
                                    )
                                ),
                                placeholder = { Text("000000") }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            LinearProgressIndicator(
                                progress = { (viewModel.codigo.length / 6f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                "${viewModel.codigo.length}/6 dígitos",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(start = 16.dp)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Botão para validar código
                            Button(
                                onClick = {
                                    viewModel.validarCodigoLocal {
                                        viewModel.setEtapa(3)
                                    }
                                },
                                enabled = !isLoading && viewModel.codigo.length == 6,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text(
                                        "Validar Código",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    // Campos de senha (visíveis apenas quando código validado)
                    AnimatedVisibility(
                        visible = etapa == 3,
                        enter = slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(500, delayMillis = 200)
                        ) + fadeIn(animationSpec = tween(300, delayMillis = 200))
                    ) {
                        Column {
                            OutlinedTextField(
                                value = viewModel.novaSenha,
                                onValueChange = { viewModel.novaSenha = it },
                                label = { Text("Nova Senha") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { mostrarNovaSenha = !mostrarNovaSenha }) {
                                        Icon(
                                            if (mostrarNovaSenha) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = if (mostrarNovaSenha) "Ocultar senha" else "Mostrar senha"
                                        )
                                    }
                                },
                                visualTransformation = if (mostrarNovaSenha) VisualTransformation.None else PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(
                                        alpha = 0.5f
                                    )
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = viewModel.confirmarSenha,
                                onValueChange = { viewModel.confirmarSenha = it },
                                label = { Text("Confirmar Nova Senha") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        mostrarConfirmarSenha = !mostrarConfirmarSenha
                                    }) {
                                        Icon(
                                            if (mostrarConfirmarSenha) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = if (mostrarConfirmarSenha) "Ocultar senha" else "Mostrar senha"
                                        )
                                    }
                                },
                                visualTransformation = if (mostrarConfirmarSenha) VisualTransformation.None else PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(
                                        alpha = 0.5f
                                    )
                                ),
                                isError = viewModel.novaSenha.isNotBlank() &&
                                        viewModel.confirmarSenha.isNotBlank() &&
                                        viewModel.novaSenha != viewModel.confirmarSenha
                            )

                            if (viewModel.novaSenha.isNotBlank() &&
                                viewModel.confirmarSenha.isNotBlank() &&
                                viewModel.novaSenha != viewModel.confirmarSenha
                            ) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "As senhas não coincidem",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Botão para alterar senha
                            Button(
                                onClick = {
                                    viewModel.confirmarNovaSenha { }
                                },
                                enabled = !isLoading && viewModel.novaSenha.isNotBlank() &&
                                        viewModel.novaSenha == viewModel.confirmarSenha,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text(
                                        "Alterar Senha",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Botão voltar
                    OutlinedButton(
                        onClick = onVoltar,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Voltar")
                    }

                    // Mensagem de erro
                    if (uiState is EsqueciMinhaSenhaUiState.Error) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = uiState.mensagem,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}