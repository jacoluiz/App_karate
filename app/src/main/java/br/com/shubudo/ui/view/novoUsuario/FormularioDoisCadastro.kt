package br.com.shubudo.ui.view.novoUsuario

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.shubudo.ui.viewModel.NovoUsuarioViewModel

@Composable
fun PaginaDoisCadastro(
    novoUsuarioViewModel: NovoUsuarioViewModel
) {
    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterAltura = remember { FocusRequester() }
    val focusRequesterPeso = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = novoUsuarioViewModel.email,
            onValueChange = { novoUsuarioViewModel.email = it },
            label = { Text("E-mail", color = MaterialTheme.colorScheme.onPrimary) },
            placeholder = {
                Text(
                    "E-mail",
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,  // Cor do texto quando o campo está em foco
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 16.dp)
                .focusRequester(focusRequesterEmail),
            keyboardActions = KeyboardActions(
                onNext = { focusRequesterAltura.requestFocus() }  // Move o foco para o próximo campo
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),

            singleLine = true,
        )

        var alturaValue by remember { mutableStateOf(TextFieldValue("0,00")) }

        TextField(
            value = alturaValue,
            singleLine = true,
            label = { Text("Altura (cm)", color = MaterialTheme.colorScheme.onPrimary) },
            placeholder = { Text("0,00", color = MaterialTheme.colorScheme.onPrimary) },

            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,  // Cor do texto quando o campo está em foco
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            ),

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),

            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequesterAltura),

            keyboardActions = KeyboardActions(
                onNext = { focusRequesterPeso.requestFocus() }  // Move o foco para o próximo campo
            ),

            onValueChange = { newValue ->
                alturaValue = applyShiftedMask(newValue)
                novoUsuarioViewModel.altura =
                    alturaValue.text // Atualiza o ViewModel com o valor formatado
            },
        )

        TextField(
            value = novoUsuarioViewModel.peso,
            onValueChange = { novoUsuarioViewModel.peso = it },
            label = { Text("Peso", color = MaterialTheme.colorScheme.onPrimary) },
            placeholder = {
                Text(
                    "Somente os quilos Ex: 72",
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,  // Cor do texto quando o campo está em foco
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            ),

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),

            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 16.dp)
                .focusRequester(focusRequesterPeso),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() } // Fecha o teclado ao pressionar "Done"
            ),
            singleLine = true,

            )
    }
}
