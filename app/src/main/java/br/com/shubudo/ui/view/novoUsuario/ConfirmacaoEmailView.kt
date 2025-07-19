package br.com.shubudo.ui.view.novoUsuario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.ui.uistate.ConfirmacaoEmailUiState
import br.com.shubudo.ui.viewModel.ConfirmacaoEmailViewModel

@Composable
fun ConfirmacaoEmailView(
    email: String,
    onConfirmado: () -> Unit,
    viewModel: ConfirmacaoEmailViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(email) {
        viewModel.email = email
    }

    var codigo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Verifique seu email", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Digite o código enviado para $email", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = codigo,
            onValueChange = {
                codigo = it
                viewModel.codigo = it
            },
            label = { Text("Código de confirmação") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                viewModel.confirmarCodigo()
            },
            enabled = codigo.length >= 6
        ) {
            if (uiState is ConfirmacaoEmailUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Confirmar")
            }
        }

        when (uiState) {
            is ConfirmacaoEmailUiState.Success -> {
                LaunchedEffect(Unit) { onConfirmado() }
            }

            is ConfirmacaoEmailUiState.Error -> {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (uiState as ConfirmacaoEmailUiState.Error).error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> Unit
        }
    }
}
