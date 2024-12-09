package br.com.shubudo.ui.view

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.R
import br.com.shubudo.ui.components.CustomIconButton
import br.com.shubudo.ui.components.DropDownMenuSelect
import br.com.shubudo.ui.components.RequisitosDeSenha
import br.com.shubudo.ui.viewModel.DropDownMenuViewModel
import br.com.shubudo.ui.viewModel.NovoUsuarioViewModel
import br.com.shubudo.ui.viewModel.ThemeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NovoUsuarioView(
    themeViewModel: ThemeViewModel,
    username: String,
    dropDownMenuViewModel: DropDownMenuViewModel,
    novoUsuarioViewModel: NovoUsuarioViewModel = NovoUsuarioViewModel(),
    onNavigateToHome: (String) -> Unit,
) {
    var isPaginaDois by remember { mutableStateOf(false) }


    if (username != "" && username.contains("@")) novoUsuarioViewModel.email =
        username else novoUsuarioViewModel.nome = username

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
                        paginaDoisCadastro(novoUsuarioViewModel)
                    } else {
                        paginaUmCadastro(
                            novoUsuarioViewModel,
                            dropDownMenuViewModel,
                            themeViewModel
                        )
                    }
                }

            }
        }
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
                        containerColor = MaterialTheme.colorScheme.primary, // Cor do container (fundo)
                        contentColor = Color.White
                    ),
                    onClick = { isPaginaDois = true }
                ) {
                    Text(
                        text = if (!isPaginaDois) "Proximo" else "Finalizar",
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 22.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun paginaUmCadastro(
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

    var senhaAtendeAosRequisitos = oitoCaracteres && contemNumero && contemCaracterEspecial
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordVisibleConfirmSenha by remember { mutableStateOf(false) }

    var isPasswordFocused by remember { mutableStateOf(false) }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    var faixaSelecionada by remember { mutableStateOf("") }

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

        DropDownMenuSelect(titulo = "Faixa", viewmodel = dropDownMenuViewModel) {
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
                            themeViewModel.changeThemeFaixa(faixa)
                            dropDownMenuViewModel.changeExpanded(false)
                            faixaSelecionada = faixa
                        },
                        cor = selecionaCorIcone(
                            faixa,
                            isSystemInDarkTheme()
                        )
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

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
                        Log.i("PasswordVisible", passwordVisible.toString())
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
                        Log.i("PasswordVisible", passwordVisibleConfirmSenha.toString())
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
        if (senhaAtendeAosRequisitos && novoUsuarioViewModel.confirmarSenha != novoUsuarioViewModel.senha && novoUsuarioViewModel.confirmarSenha.isNotEmpty()) {
            Column(modifier = Modifier.padding(start = 0.dp)) {
                RequisitosDeSenha(text = "Senha não confere", isMet = false, bringIntoViewRequester)
            }
        } else if (!senhaAtendeAosRequisitos && novoUsuarioViewModel.confirmarSenha.isNotEmpty()) {
            RequisitosDeSenha(
                text = "Sua senha não atende aos requisitos",
                isMet = false,
                bringIntoViewRequester
            )

        }
    }
}


@Composable
fun paginaDoisCadastro(
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