package br.com.shubudo.ui.view

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import br.com.shubudo.ui.viewModel.ThemeViewModel

@Composable
fun LoginView(
    onNavigateToHome: (String) -> Unit,
    themeViewModel: ThemeViewModel
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var conter by remember { mutableStateOf(0) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Parte superior da tela com o título
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Shubu-dô",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.padding(top = 32.dp)
                )
                Text(
                    text = "Faça seu login para prosseguir",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Spacer(modifier = Modifier.height(160.dp))

            // Card central com campos de login e botões
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        label = {
                            Text(
                                color = MaterialTheme.colorScheme.onPrimary,
                                text = "Usuário"
                            )
                        },
                        placeholder = {
                            Text(
                                color = MaterialTheme.colorScheme.onPrimary,
                                text = "Digite seu usuário"
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = {
                            Text(
                                color = MaterialTheme.colorScheme.onPrimary,
                                text = "Senha"
                            )
                        },
                        placeholder = {
                            Text(
                                color = MaterialTheme.colorScheme.onPrimary,
                                text = "Digite sua senha"
                            )
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )


                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                onNavigateToHome("Avisos")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp)
                        ) {
                            Text("Novo por aqui?")
                        }
                        Button(
                            onClick = {
                                onNavigateToHome("Avisos")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        ) {
                            Text("Login")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    Log.i("EsqueciSenha", conter.toString())
                    conter++
                    if (conter == 0) {
                        themeViewModel.changeThemeFaixa("Branca")
                    } else if (conter == 1) {
                        themeViewModel.changeThemeFaixa("Amarela")
                    } else if (conter == 2) {
                        themeViewModel.changeThemeFaixa("Laranja")
                    } else if (conter == 3) {
                        themeViewModel.changeThemeFaixa("Verde")
                    } else if (conter == 4) {
                        themeViewModel.changeThemeFaixa("Roxa")
                    } else if (conter == 5) {
                        themeViewModel.changeThemeFaixa("Marrom")
                        conter = -1
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
            ) {
                Text("Esqueci minha senha?")
            }

        }
    }
}