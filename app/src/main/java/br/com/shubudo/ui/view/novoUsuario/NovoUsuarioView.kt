package br.com.shubudo.ui.view.novoUsuario

import PaginaUmCadastro
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
    var showDialog by remember { mutableStateOf(false) }
    var isPaginaDois by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Seja bem-vindo ${if (username.isNotEmpty()) "${novoUsuarioViewModel.nome} " else ""}ao Shubu-dô APP! Precisamos de alguns dados para continuar.",
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            elevation = CardDefaults.cardElevation(8.dp)

        ) {
            Column(

                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                // Adicione o AnimatedContent aqui
                AnimatedContent(
                    targetState = isPaginaDois,
                    transitionSpec = {
                        if (targetState) {
                            (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                                slideOutHorizontally { width -> -width } + fadeOut())
                        } else {
                            (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                                slideOutHorizontally { width -> width } + fadeOut())
                        }
                    }, label = ""
                ) { targetState ->
                    if (targetState) {
                        PaginaDoisCadastro(novoUsuarioViewModel)
                    } else {
                        PaginaUmCadastro(
                            novoUsuarioViewModel,
                            dropDownMenuViewModel,
                            themeViewModel
                        )
                    }
                }
            }
        }
        Text(
            "Preencha os campos para contiunuar",
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Row {
            AnimatedVisibility(
                visible = isPaginaDois,
                exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -50 })
            ) {
                TextButton(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = { isPaginaDois = false }
                ) {
                    Text(
                        text = "Voltar",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 22.dp)
                    )
                }
            }
            AnimatedVisibility(
                visible = true,
                exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -50 })
            ) {
                ElevatedButton(
                    modifier = Modifier.padding(top = 16.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    onClick = {
                        if ( validarPaginaUmCompleta(novoUsuarioViewModel) && !isPaginaDois) {
                            isPaginaDois = true
                        } else if ( validarPaginaUmCompleta(novoUsuarioViewModel) && isPaginaDois) {
                            novoUsuarioViewModel.cadastrarUsuario()
                            showDialog = true
                        }
                    },
                ) {
                    Text(
                        text = if (!isPaginaDois) "Proximo" else "Finalizar",
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 22.dp)
                    )
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showDialog = false
                                    onNavigateToLogin(novoUsuarioViewModel.nome)
                                }) {
                                Text("OK")
                            }
                        },
                        title = { Text("Cadastro realizado com sucesso") },
                        text = {
                            Text(
                                "Seu cadastro foi criado com sucesso! Agora é só aguardar a autorização do administrador. Fale com o Jacó para ser mais rapido"
                            )
                        }
                    )
                }

            }
        }
    }
}

fun validarPaginaUmCompleta(viewModel: NovoUsuarioViewModel): Boolean {
    return viewModel.nome.isNotBlank() &&
            viewModel.senhaAtendeAosRequisitos &&
            viewModel.faixa.isNotBlank() &&
            viewModel.senha == viewModel.confirmarSenha
}

fun validarPaginaDoisCompleta(viewModel: NovoUsuarioViewModel): Boolean {
    return viewModel.nome.isNotBlank() &&
            viewModel.senhaAtendeAosRequisitos &&
            viewModel.faixa.isNotBlank() &&
            viewModel.senha == viewModel.confirmarSenha &&
            viewModel.peso.isNotBlank() &&
            viewModel.altura.isNotBlank() &&
            viewModel.email.isNotBlank()

}

// Função para aplicar a máscara #,##
fun applyShiftedMask(input: TextFieldValue): TextFieldValue {
    // Filtra apenas os dígitos
    val digits = input.text.filter { it.isDigit() }

    // Limita o número de dígitos a três
    val limitedDigits = digits.takeLast(3)

    // Formata o texto de acordo com a máscara "0,00"
    val maskedText = when (limitedDigits.length) {
        1 -> "0,0${limitedDigits[0]}"
        2 -> "0,${limitedDigits}"
        3 -> "${limitedDigits[0]},${limitedDigits.substring(1, 3)}"
        else -> "0,00"
    }

    // Cursor posicionado no final
    return TextFieldValue(maskedText, TextRange(maskedText.length))
}