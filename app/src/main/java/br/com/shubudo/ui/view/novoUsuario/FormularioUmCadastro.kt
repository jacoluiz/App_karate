import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import br.com.shubudo.R
import br.com.shubudo.ui.components.CustomIconButton
import br.com.shubudo.ui.components.DropDownMenuSelect
import br.com.shubudo.ui.components.RequisitosDeSenha
import br.com.shubudo.ui.view.selecionaCorIcone
import br.com.shubudo.ui.viewModel.DropDownMenuViewModel
import br.com.shubudo.ui.viewModel.NovoUsuarioViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaginaUmCadastro(
    novoUsuarioViewModel: NovoUsuarioViewModel,
    dropDownMenuViewModel: DropDownMenuViewModel,
    themeViewModel: ThemeViewModel
) {

    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    // Requisitos de senha
    val oitoCaracteres = novoUsuarioViewModel.senha.length >= 8
    val contemNumero = novoUsuarioViewModel.senha.any { it.isDigit() }
    val contemCaracterEspecial = novoUsuarioViewModel.senha.any { !it.isLetterOrDigit() }

    novoUsuarioViewModel.senhaAtendeAosRequisitos =
        oitoCaracteres && contemNumero && contemCaracterEspecial
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordVisibleConfirmSenha by remember { mutableStateOf(false) }

    var isPasswordFocused by remember { mutableStateOf(false) }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    val faixas = listOf(
        "Branca",
        "Amarela",
        "Laranja",
        "Verde",
        "Roxa",
        "Marrom",
        "Preta"
    )

    val focusRequesterNome = remember { FocusRequester() }
    val focusRequesterSenha = remember { FocusRequester() }
    val focusRequesterConfirmarSenha = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DropDownMenuSelect(titulo = novoUsuarioViewModel.faixa, viewmodel = dropDownMenuViewModel) {
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            ) {

                faixas.forEach { faixa ->
                    val iconPainter =
                        if (faixa == "Branca" && !isSystemInDarkTheme()) {
                            painterResource(id = R.drawable.ic_faixa_outline)
                        } else {
                            painterResource(id = R.drawable.ic_faixa)
                        }
                    CustomIconButton(
                        texto = faixa,
                        iconPainter = iconPainter,
                        onClick = {
                            val tempNome = novoUsuarioViewModel.nome
                            val tempSenha = novoUsuarioViewModel.senha
                            val tempConfirmarSenha = novoUsuarioViewModel.confirmarSenha

                            dropDownMenuViewModel.changeExpanded(false)
                            dropDownMenuViewModel.changeSelected(true)
                            dropDownMenuViewModel.changeFaixaSelecionada(faixa)

                            novoUsuarioViewModel.faixa = faixa
                            themeViewModel.changeThemeFaixa(faixa)

                            novoUsuarioViewModel.nome = tempNome
                            novoUsuarioViewModel.senha = tempSenha
                            novoUsuarioViewModel.confirmarSenha = tempConfirmarSenha
                        },
                        cor = selecionaCorIcone(
                            faixa,
                            isSystemInDarkTheme()
                        )
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(4.dp))

        TextField(
            singleLine = true,
            value = novoUsuarioViewModel.nome,
            onValueChange = { novoUsuarioViewModel.nome = it },
            label = { Text("Nome", color = MaterialTheme.colorScheme.onPrimary) },
            placeholder = {
                Text(
                    "Nome completo",
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
                .focusRequester(focusRequesterNome)
                .padding(bottom = 16.dp, top = 16.dp),

            keyboardActions = KeyboardActions(
                onNext = { focusRequesterSenha.requestFocus() }  // Move o foco para o próximo campo
            ),

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
        )

        TextField(
            singleLine = true,
            value = novoUsuarioViewModel.senha,
            onValueChange = { novoUsuarioViewModel.senha = it },
            label = { Text("Senha", color = MaterialTheme.colorScheme.onPrimary) },
            placeholder = { Text("Senha", color = MaterialTheme.colorScheme.onPrimary) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusRequesterConfirmarSenha.requestFocus() }  // Move o foco para o próximo campo
            ),
            trailingIcon = {
                val image =
                    if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
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
                .focusRequester(focusRequesterSenha)
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .onFocusChanged { focusState ->
                    isPasswordFocused = focusState.isFocused
                }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Exibir requisitos apenas quando o campo de senha está em foco
        if ((isPasswordFocused || novoUsuarioViewModel.senha.isNotEmpty()) && (!oitoCaracteres || !contemNumero || !contemCaracterEspecial)) {
            Column(modifier = Modifier.padding(start = 0.dp)) {
                RequisitosDeSenha(
                    text = "Pelo menos 8 caracteres",
                    isMet = oitoCaracteres,
                    bringIntoViewRequester
                )
                RequisitosDeSenha(
                    text = "Contém número",
                    isMet = contemNumero,
                    bringIntoViewRequester
                )
                RequisitosDeSenha(
                    text = "Contém caracter especial",
                    isMet = contemCaracterEspecial,
                    bringIntoViewRequester
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        TextField(
            singleLine = true,
            value = novoUsuarioViewModel.confirmarSenha,
            onValueChange = { novoUsuarioViewModel.confirmarSenha = it },
            label = {
                Text(
                    "Confirmar senha",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            placeholder = {
                Text(
                    "Confirme sua senha",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            visualTransformation = if (passwordVisibleConfirmSenha) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() } // Fecha o teclado ao pressionar "Done"
            ),
            trailingIcon = {
                val image =
                    if (passwordVisibleConfirmSenha) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(
                    onClick = {
                        passwordVisibleConfirmSenha = !passwordVisibleConfirmSenha
                    }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (passwordVisibleConfirmSenha) "Ocultar senha" else "Mostrar senha",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
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
                .padding(bottom = 16.dp)
                .bringIntoViewRequester(bringIntoViewRequester)
                .focusRequester(focusRequesterConfirmarSenha)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                },
        )
        if (novoUsuarioViewModel.senhaAtendeAosRequisitos && novoUsuarioViewModel.confirmarSenha != novoUsuarioViewModel.senha && novoUsuarioViewModel.confirmarSenha.isNotEmpty()) {
            Column(modifier = Modifier.padding(start = 0.dp)) {
                RequisitosDeSenha(text = "Senha não confere", isMet = false, bringIntoViewRequester)
            }
        } else if (!novoUsuarioViewModel.senhaAtendeAosRequisitos && novoUsuarioViewModel.confirmarSenha.isNotEmpty()) {
            RequisitosDeSenha(
                text = "Sua senha não atende aos requisitos",
                isMet = false,
                bringIntoViewRequester
            )

        }
    }
}