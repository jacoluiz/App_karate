package br.com.shubudo.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.SessionManager
import br.com.shubudo.ui.uistate.DetalheAvisoUiState
import br.com.shubudo.ui.viewModel.DetalheAvisoViewModel
import coil.compose.rememberAsyncImagePainter

@Composable
fun DetalheAvisoView(
    uiState: DetalheAvisoUiState,
    onNavigationPop: () -> Unit // Callback para pop na navegação após a exclusão
) {
    val viewModel: DetalheAvisoViewModel = hiltViewModel()
    val imagemDefault = "https://karateshubudo.com.br/wp-content/uploads/elementor/thumbs/LOGO_KARATE-JPG-2010-01-01-pgd3sk0v62xv53x0bhlsbq53k0unw3zph03j682rww.png"
    // Estado para controlar a exibição do diálogo de confirmação de exclusão
    val showDeleteConfirm = remember { mutableStateOf(false) }

    when (uiState) {
        is DetalheAvisoUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is DetalheAvisoUiState.Success -> {
            Column {
                // Exibe a imagem do aviso (ou a imagem default)
                Image(
                    painter = rememberAsyncImagePainter(
                        model = if (uiState.aviso.imagem.isNullOrBlank()) imagemDefault
                        else uiState.aviso.imagem
                    ),
                    contentDescription = "Imagem do Aviso",
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
                // Botão de delete, visível somente para administradores
                if (SessionManager.usuarioLogado?.perfil?.equals("adm", ignoreCase = true) == true) {
                    IconButton(
                        onClick = { showDeleteConfirm.value = true },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Deletar Aviso",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
                // Diálogo de confirmação para exclusão
                if (showDeleteConfirm.value) {
                    AlertDialog(
                        onDismissRequest = { showDeleteConfirm.value = false },
                        title = { Text("Confirmar Exclusão") },
                        text = { Text("Tem certeza que deseja excluir este aviso?") },
                        confirmButton = {
                            TextButton(onClick = {
                                showDeleteConfirm.value = false
                                viewModel.deleteAviso(
                                    onSuccess = { onNavigationPop() },
                                    onError = { errorMsg ->

                                        println("Erro ao deletar aviso: $errorMsg")
                                    }
                                )
                            }) {
                                Text("Sim")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDeleteConfirm.value = false }) {
                                Text("Não")
                            }
                        }
                    )
                }
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = uiState.aviso.titulo,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.aviso.conteudo,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
        is DetalheAvisoUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
