package br.com.shubudo.ui.view.recursos.parceiros

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.R
import br.com.shubudo.SessionManager.usuarioLogado
import br.com.shubudo.model.Parceiro
import br.com.shubudo.ui.components.CabecalhoComIconeCentralizado
import br.com.shubudo.ui.components.LoadingWrapper
import br.com.shubudo.ui.uistate.ParceirosUiState
import br.com.shubudo.ui.viewModel.ParceiroViewModel
import coil.compose.AsyncImage

@Composable
fun ParceirosView(
    onNavigateToEditarParceiro: (String) -> Unit = {},
    onNavigateToCadastroParceiro: () -> Unit = {},
    onNavigateToParceiroDetalhe: (String) -> Unit = {},
    viewModel: ParceiroViewModel = hiltViewModel()
) {
    val uiState by viewModel.parceirosUiState.collectAsState()
    var searchQuery by remember { mutableStateOf(TextFieldValue()) }

    LoadingWrapper(
        isLoading = uiState is ParceirosUiState.Loading,
        loadingText = "Carregando parceiros..."
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CabecalhoComIconeCentralizado(
                titulo = "Parceiros",
                subtitulo = "Conheça nossos apoiadores",
                iconeAndroid = Icons.Default.Business
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar parceiros...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    IconButton(onClick = viewModel::recarregarParceiros) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recarregar")
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (uiState) {
                    is ParceirosUiState.Success -> {
                        val parceirosFiltrados =
                            (uiState as ParceirosUiState.Success).parceiros.filter {
                                it.nome.contains(searchQuery.text, ignoreCase = true)
                            }
                        items(parceirosFiltrados) { parceiro ->
                            ParceiroItem(
                                parceiro = parceiro,
                                onClick = {
                                    onNavigateToParceiroDetalhe(parceiro._id ?: "")
                                })
                        }
                    }

                    is ParceirosUiState.Empty -> {
                        item {
                            Text(
                                text = "Nenhum parceiro encontrado.",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    else -> Unit
                }
            }

            if (usuarioLogado?.perfis?.contains("adm") == true) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    FloatingActionButton(
                        onClick = onNavigateToCadastroParceiro,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Novo parceiro")
                    }
                }
            }
        }
    }
}

@Composable
fun ParceiroItem(parceiro: Parceiro, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = parceiro.logomarca,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.ic_launcher),
                error = painterResource(id = R.drawable.ic_launcher),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = parceiro.nome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (parceiro.localizacao?.isNotBlank() == true) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = parceiro.localizacao,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (parceiro.telefone.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Telefone: ${parceiro.telefone}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
