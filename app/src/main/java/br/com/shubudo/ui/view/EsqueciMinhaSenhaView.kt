package br.com.shubudo.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.ui.viewModel.EsqueciMinhaSenhaViewModel

@Composable
fun EsqueciMinhaSenhaView(
    username: String = "",
    onSendResetRequest: () -> Boolean = { true },
    onSenhaRedefinida: () -> Unit
) {
    val viewModel = hiltViewModel<EsqueciMinhaSenhaViewModel>()

    LaunchedEffect(username) {
        if (username.isNotEmpty()) {
            viewModel.email.value = username
        }
    }

    val etapa by remember { viewModel.etapa }
    val sucesso by remember { viewModel.sucesso }
    val erro by remember { viewModel.mensagemErro }

    if (sucesso) {
        AlertDialog(
            onDismissRequest = { onSenhaRedefinida() },
            confirmButton = {
                TextButton(onClick = onSenhaRedefinida) {
                    Text("OK")
                }
            },
            title = { Text("Sucesso") },
            text = { Text("Senha redefinida com sucesso!") }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (etapa == 1) {
            EtapaUm(viewModel)
        } else {
            EtapaDois(viewModel)
        }
        erro?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = Color.Red)
        }
    }
}


@Composable
fun EtapaUm(viewModel: EsqueciMinhaSenhaViewModel) {
    val email = viewModel.email.value

    OutlinedTextField(
        value = email,
        onValueChange = { viewModel.email.value = it },
        label = { Text("E-mail") }
    )
    Spacer(Modifier.height(16.dp))
    Button(onClick = { viewModel.solicitarCodigo() }) {
        Text("Enviar código")
    }
}

@Composable
fun EtapaDois(viewModel: EsqueciMinhaSenhaViewModel) {
    val codigo = viewModel.codigo.value
    val novaSenha = viewModel.novaSenha.value

    OutlinedTextField(
        value = codigo,
        onValueChange = { viewModel.codigo.value = it },
        label = { Text("Código") }
    )
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(
        value = novaSenha,
        onValueChange = { viewModel.novaSenha.value = it },
        label = { Text("Nova senha") },
        visualTransformation = PasswordVisualTransformation()
    )
    Spacer(Modifier.height(16.dp))
    Button(onClick = { viewModel.redefinirSenha() }) {
        Text("Redefinir senha")
    }
}
