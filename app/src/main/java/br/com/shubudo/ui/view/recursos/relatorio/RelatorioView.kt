package br.com.shubudo.ui.view.recursos.relatorio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.ui.components.CabecalhoComIconeCentralizado
import br.com.shubudo.ui.uistate.RelatorioUiState
import br.com.shubudo.ui.viewModel.RelatorioViewModel

data class ReportAction(
    val titulo: String,
    val descricao: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true
)

@Composable
fun RelatoriosView(
    viewModel: RelatorioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Feedback após sucesso/erro
    LaunchedEffect(uiState) {
        when (uiState) {
            is RelatorioUiState.Success -> {
                val name = (uiState as RelatorioUiState.Success).fileName
                snackbar.showSnackbar("Relatório salvo em Downloads: $name")

                viewModel.reset()
            }

            is RelatorioUiState.Error -> {
                val msg = (uiState as RelatorioUiState.Error).message
                snackbar.showSnackbar("Erro: $msg")
                viewModel.reset()
            }

            else -> Unit
        }
    }

    val isDownloading = uiState is RelatorioUiState.Downloading

    // Defina os botões (relatórios) aqui
    val acoes = listOf(
        ReportAction(
            titulo = "Organização Exame",
            descricao = "Gera planilha com cones/filas e chamadas por faixa, altura e academia.",
            onClick = {
                viewModel.baixarRelatorioOrganizado(
                    context = context
                )
            },
            enabled = !isDownloading
        )
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbar) }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                CabecalhoComIconeCentralizado(
                    titulo = "Relatórios",
                    subtitulo = "Gere planilhas direto do app",
                    iconeAndroid = Icons.Default.Assessment
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-20).dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Ações rápidas",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (isDownloading) {
                            Spacer(Modifier.height(12.dp))
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Baixando relatório...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(acoes) { acao ->
                                ReportActionCard(acao = acao)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportActionCard(
    acao: ReportAction
) {
    Card(
        onClick = { if (acao.enabled) acao.onClick() },
        enabled = acao.enabled,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(Modifier.height(8.dp))
            Text(text = acao.titulo, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(
                text = acao.descricao,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 5
            )
        }
    }
}
