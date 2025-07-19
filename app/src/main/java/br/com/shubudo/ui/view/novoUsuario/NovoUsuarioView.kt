package br.com.shubudo.ui.view.novoUsuario

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.ui.uistate.CadastroUiState
import br.com.shubudo.ui.viewModel.DropDownMenuViewModel
import br.com.shubudo.ui.viewModel.NovoUsuarioViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel

@Composable
fun NovoUsuarioView(
    themeViewModel: ThemeViewModel,
    username: String,
    dropDownMenuViewModel: DropDownMenuViewModel,
    novoUsuarioViewModel: NovoUsuarioViewModel = hiltViewModel(),
    onNavigateToLogin: (String) -> Unit,
) {
    val uiState by novoUsuarioViewModel.uiState.collectAsState()
    var isPaginaDois by remember { mutableStateOf(false) }
    val currentPage = if (isPaginaDois) 2 else 1

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when (uiState) {
            is CadastroUiState.Success -> {
                dialogMessage = (uiState as CadastroUiState.Success).message
                isSuccess = true
                showDialog = true
            }

            is CadastroUiState.Error -> {
                dialogMessage = (uiState as CadastroUiState.Error).error
                isSuccess = false
                showDialog = true
            }

            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderSection(username, novoUsuarioViewModel.nome)

        Spacer(modifier = Modifier.height(24.dp))

        ProgressIndicator(currentPage = currentPage)

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            AnimatedContent(
                targetState = isPaginaDois,
                transitionSpec = {
                    if (targetState) {
                        slideInHorizontally { it } + fadeIn() togetherWith
                                slideOutHorizontally { -it } + fadeOut()
                    } else {
                        slideInHorizontally { -it } + fadeIn() togetherWith
                                slideOutHorizontally { it } + fadeOut()
                    }
                },
                label = "CadastroAnimation"
            ) { target ->
                if (target) {
                    PaginaDoisCadastro(novoUsuarioViewModel)
                } else {
                    PaginaUmCadastro(novoUsuarioViewModel, dropDownMenuViewModel, themeViewModel)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        NavigationButtons(
            currentPage = currentPage,
            onPrevious = { isPaginaDois = false },
            onNext = {
                if (isPaginaDois) {
                    novoUsuarioViewModel.cadastrarUsuario()
                } else {
                    isPaginaDois = true
                }
            },
            isNextEnabled = if (isPaginaDois)
                validarPaginaDoisCompleta(novoUsuarioViewModel)
            else
                validarPaginaUmCompleta(novoUsuarioViewModel)
        )

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        if (isSuccess) {
                            onNavigateToLogin(novoUsuarioViewModel.email)
                        }
                    }) {
                        Text("OK")
                    }
                },
                title = {
                    Text(
                        if (isSuccess) "Cadastro realizado com sucesso"
                        else "Erro no cadastro"
                    )
                },
                text = { Text(dialogMessage) }
            )
        }
    }
}


@Composable
private fun HeaderSection(username: String, userName: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Seja bem-vindo ao Shubu-dô APP!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Precisamos de alguns dados para continuar.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ProgressIndicator(currentPage: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Step 1
        ProgressStep(
            stepNumber = 1,
            title = "Dados Pessoais",
            isActive = currentPage == 1,
            isCompleted = currentPage > 1
        )

        // Connector
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(2.dp)
                .background(
                    color = if (currentPage > 1)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(1.dp)
                )
        )

        // Step 2
        ProgressStep(
            stepNumber = 2,
            title = "Informações Físicas",
            isActive = currentPage == 2,
            isCompleted = false
        )
    }
}

@Composable
private fun ProgressStep(
    stepNumber: Int,
    title: String,
    isActive: Boolean,
    isCompleted: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isCompleted -> MaterialTheme.colorScheme.primary
                        isActive -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Text(
                    text = "✓",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = stepNumber.toString(),
                    color = if (isActive)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
            ),
            color = if (isActive)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun NavigationButtons(
    currentPage: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    isNextEnabled: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = currentPage == 2,
            enter = slideInHorizontally { -it } + fadeIn(),
            exit = slideOutHorizontally { -it } + fadeOut()
        ) {
            OutlinedButton(
                onClick = onPrevious,
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Voltar",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        Button(
            onClick = onNext,
            enabled = isNextEnabled,
            modifier = Modifier
                .height(56.dp)
                .widthIn(min = 120.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = if (currentPage == 1) "Próximo" else "Finalizar",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

fun validarPaginaUmCompleta(viewModel: NovoUsuarioViewModel): Boolean {
    return viewModel.nome.isNotBlank() &&
            viewModel.faixa.isNotBlank() &&
            viewModel.senha.isNotBlank() &&
            viewModel.confirmarSenha.isNotBlank() &&
            viewModel.senha == viewModel.confirmarSenha &&
            viewModel.senhaAtendeAosRequisitos
}

fun validarPaginaDoisCompleta(viewModel: NovoUsuarioViewModel): Boolean {
    return viewModel.email.isNotBlank() &&
            viewModel.altura.isNotBlank() &&
            viewModel.altura != "0,00" &&
            viewModel.peso.isNotBlank()
}