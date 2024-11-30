package br.com.shubudo.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.R
import br.com.shubudo.ui.uistate.PerfilUiState

@Composable
fun PerfilView(
    uiState: PerfilUiState,
    onLogout: () -> Unit
) {
//    when (uiState) {
//        is PerfilUiState.Loading -> {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        }
//
//        is PerfilUiState.Empty -> {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "Nenhum dado encontrado.",
//                    style = MaterialTheme.typography.bodyMedium,
//                    textAlign = TextAlign.Center
//                )
//            }
//        }
//
//        is PerfilUiState.Success -> {
//            PerfilContent(
//                nome = uiState.nome,
//                username = uiState.username,
//                email = uiState.email,
//                corFaixa = uiState.corFaixa,
//                onLogout = onLogout
//            )
//        }
//    }
}

@Composable
fun PerfilContent(
    nome: String,
    username: String,
    email: String,
    corFaixa: String,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Imagem de Perfil
        Image(
            painter = painterResource(R.drawable.ic_projecoes), // Substitua pelo recurso adequado
            contentDescription = "Imagem de Perfil",
            modifier = Modifier
                .size(120.dp)
                .padding(8.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.size(16.dp))

        // Nome do Usuário
        Text(
            text = nome,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Nome de usuário
        Text(
            text = "@$username",
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

        // Faixa
        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Faixa: ",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = corFaixa,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.size(32.dp))

        // Botão de Logout
        Button(onClick = onLogout) {
            Text(text = "Sair")
        }
    }
}
