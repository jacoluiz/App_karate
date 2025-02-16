package br.com.shubudo.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.R
import br.com.shubudo.ui.uistate.PerfilUiState
import br.com.shubudo.ui.viewModel.ThemeViewModel

@Composable
fun PerfilView(
    uiState: PerfilUiState,
    themeViewModel: ThemeViewModel,
    onLogout: () -> Unit,
    onEditarPerfil: () -> Unit
) {
    when (uiState) {
        is PerfilUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is PerfilUiState.Empty -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum dado encontrado.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        is PerfilUiState.Success -> {
            themeViewModel.changeThemeFaixa(uiState.corFaixa)
            PerfilContent(
                nome = uiState.nome,
                username = uiState.username,
                email = uiState.email,
                corFaixa = uiState.corFaixa,
                idade = uiState.idade,
                peso = uiState.peso,
                onLogout = onLogout,
                onEditarPerfil = onEditarPerfil
            )
        }
    }
}

@Composable
fun PerfilContent(
    nome: String,
    username: String,
    email: String,
    idade: String,
    peso: String,
    corFaixa: String,
    onLogout: () -> Unit,
    onEditarPerfil: () -> Unit
) {


    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        // Imagem de Perfil
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(16.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_sequencia_de_combate), // Substitua pelo recurso adequado
                    contentDescription = "Imagem de Perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(0.dp),
                    contentScale = ContentScale.Crop
                )
                Column {
                    // Nome do Usuário
                    Text(
                        text = nome,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Nome de usuário
                    Text(
                        text = username,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    // E-mail
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Idade: $idade",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Peso: $peso",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()

        ) {
            // Botão de Logout
            Button(
                onClick = {
                    onEditarPerfil()
                }) {
                Text(text = "Editar Perfil")
            }

            Spacer(modifier = Modifier.size(160.dp))
            Button(
                onClick = {
                    onLogout()
                }) {
                Text(text = "Logout")
            }
        }

    }
}
