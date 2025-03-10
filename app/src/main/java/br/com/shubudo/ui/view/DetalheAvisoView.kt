package br.com.shubudo.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
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
    onNavigationPop: () -> Unit, // Callback para voltar à tela após exclusão
    onEdit: (Any?) -> Unit          // Callback para iniciar a edição do aviso
) {
    val viewModel: DetalheAvisoViewModel = hiltViewModel()
    val imagemDefault =
        "https://karateshubudo.com.br/wp-content/uploads/elementor/thumbs/LOGO_KARATE-JPG-2010-01-01-pgd3sk0v62xv53x0bhlsbq53k0unw3zph03j682rww.png"
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
            Column(modifier = Modifier.fillMaxSize()) {
                // Imagem do aviso
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
                // Botões de ação (Editar e Deletar) para administradores
                if (SessionManager.usuarioLogado?.perfil?.equals("adm", ignoreCase = true) == true) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { onEdit(uiState.aviso._id) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar Aviso",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            onClick = { showDeleteConfirm.value = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Deletar Aviso",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
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
                                        // Trate o erro conforme necessário, por exemplo, exiba uma mensagem de alerta.
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
                // Área de conteúdo com scroll para textos longos
                Surface(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
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
