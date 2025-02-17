package br.com.shubudo.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.R
import br.com.shubudo.ui.uistate.PerfilUiState
import br.com.shubudo.ui.viewModel.ThemeViewModel
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

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
            // Ajusta o tema conforme a faixa do usuário
            themeViewModel.changeThemeFaixa(uiState.corFaixa)

            PerfilContent(
                nome = uiState.nome,
                username = uiState.username,
                email = uiState.email,
                corFaixa = uiState.corFaixa,
                idade = uiState.idade,
                peso = uiState.peso,
                altura = uiState.altura,
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
    corFaixa: String,
    idade: String,
    peso: String,
    altura: String,
    onLogout: () -> Unit,
    onEditarPerfil: () -> Unit
) {
    // Topo colorido com foto, nome e faixa
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // altura do cabeçalho
            .background(MaterialTheme.colorScheme.primary)
    ) {
        // Coluna que centraliza o conteúdo no topo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto de perfil circular
            Image(
                painter = painterResource(id = R.drawable.ic_sequencia_de_combate),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Nome do usuário
            Text(
                text = nome,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            // Faixa do usuário
            Text(
                text = "Faixa $corFaixa",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    // Conteúdo principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        // Card com as informações pessoais
        Card(
            modifier = Modifier
                .padding(top = 150.dp) // para ficar abaixo do topo colorido
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Informações Pessoais",
                    style = MaterialTheme.typography.titleMedium
                )
                // Email
                RowInfo(label = "Email", value = email)
                // Peso
                RowInfo(label = "Peso", value = "$peso kg")
                // Altura
                RowInfo(label = "Altura", value = "$altura cm")
                // Idade
                RowInfo(label = "Idade", value = idade)
            }
        }

        // Botão de Editar Perfil
        Button(
            onClick = onEditarPerfil,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Editar Perfil")
        }

        // Botão de Logout
        Button(
            onClick = onLogout,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Logout")
        }
    }
}

@Composable
fun RowInfo(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}
