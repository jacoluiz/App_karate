package br.com.shubudo.ui.view.recursos

import CampoDeTextoPadrao
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Announcement
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.R
import br.com.shubudo.SessionManager
import br.com.shubudo.SessionManager.perfilAtivo
import br.com.shubudo.SessionManager.usuarioLogado
import br.com.shubudo.ui.components.CabecalhoComIconeCentralizado
import br.com.shubudo.ui.viewModel.RecursosViewModel

data class RecursoItem(
    val titulo: String,
    val icon: RecursoIcon,
    val onClick: () -> Unit
)

sealed class RecursoIcon {
    data class Vector(val icon: ImageVector) : RecursoIcon()
    data class PainterIcon(val icon: Painter) : RecursoIcon()
}

@Composable
fun RecursosView(
    onNavigateToAvisos: () -> Unit = {},
    onNavigateToEventos: () -> Unit = {},
    onNavigateToProgramacao: () -> Unit = {},
    onNavigateToAcademias: () -> Unit = {},
    onNavigateToBaseUsuarios: () -> Unit = {},
    onNavigateToGaleria: () -> Unit = {},
    onNavigateToParceiros: () -> Unit = {},
    onNavigateToRelatorios: () -> Unit = {},
    viewModel: RecursosViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val nomesAcademias by viewModel.nomesAcademias.collectAsState()
    val nomeAcademiaSelecionada = nomesAcademias[SessionManager.idAcademiaVisualizacao].orEmpty()

    LaunchedEffect(Unit) {
        viewModel.atualizarUsuarioLogado(context)
        viewModel.carregarNomesAcademias()
    }

    val recursos = buildList {

        add(
            RecursoItem(
                "Programação",
                RecursoIcon.Vector(Icons.Default.SportsMartialArts),
                onNavigateToProgramacao
            )
        )

        add(
            RecursoItem(
                "Parceiros",
                RecursoIcon.Vector(Icons.Filled.Business),
                onNavigateToParceiros
            )
        )

        // Se o usuário está logado
        if (usuarioLogado != null) {
            add(
                RecursoItem(
                    "Avisos",
                    RecursoIcon.Vector(Icons.AutoMirrored.Filled.Announcement),
                    onNavigateToAvisos
                )
            )

            add(
                RecursoItem(
                    "Galeria",
                    RecursoIcon.PainterIcon(painterResource(R.drawable.ic_galeria)),
                    onNavigateToGaleria
                )
            )

            add(
                RecursoItem(
                    "Eventos",
                    RecursoIcon.Vector(Icons.Default.Event),
                    onNavigateToEventos
                )
            )

        }

        if (usuarioLogado?.perfis?.contains("adm") == true) {
            add(
                RecursoItem(
                    "Academias",
                    RecursoIcon.PainterIcon(painterResource(R.drawable.ic_academia)),
                    onNavigateToAcademias
                )
            )
        }

        if (usuarioLogado?.perfis?.contains("adm") == true || perfilAtivo.contains("professor")) {
            add(
                RecursoItem(
                    "Base de Usuários",
                    RecursoIcon.Vector(Icons.Filled.AccountBox),
                    onNavigateToBaseUsuarios
                )
            )

            add(
                RecursoItem(
                    "Relatorios",
                    RecursoIcon.Vector(Icons.Filled.BarChart),
                    onNavigateToRelatorios
                )
            )
        }
    }.sortedBy { it.titulo }

    Column(modifier = Modifier.fillMaxSize()) {
        // Cabeçalho com fundo gradiente
        CabecalhoComIconeCentralizado(
            titulo = "Recursos do Karate",
            subtitulo = "Acesse materiais complementares e informações importantes",
            iconeAndroid = Icons.AutoMirrored.Filled.LibraryBooks
        )

        // Conteúdo principal com grid de recursos
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val nomesAcademias by viewModel.nomesAcademias.collectAsState()

                    // Botão para alternar perfil - só mostra se o usuário tem perfil professor
                    if (usuarioLogado?.perfis?.contains("professor") == true) {
                        var showDialog by remember { mutableStateOf(false) }
                        var searchText by remember { mutableStateOf("") }

                        val opcoes = remember(usuarioLogado, nomesAcademias) {
                            val opcoesBase = mutableListOf<Pair<String, String>>()

                            // Opção "aluno"
                            val nomeFilialAluno = usuarioLogado?.filialId?.let { id ->
                                nomesAcademias[usuarioLogado!!.academiaId] ?: "Desconhecida"
                            } ?: "Desconhecida"
                            opcoesBase.add("Aluno ($nomeFilialAluno)" to usuarioLogado?.academiaId.orEmpty())

                            // Opções "professor"
                            usuarioLogado?.professorEm?.forEachIndexed { index, idAcademia ->
                                val nome = nomesAcademias[idAcademia] ?: "Desconhecida"
                                opcoesBase.add("Prof. ($nome)" to idAcademia)
                            }

                            opcoesBase
                        }
                        Spacer(modifier = Modifier.weight(1f)) // força o botão a ir para a direita

                        Button(
                            onClick = {
                                showDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .height(32.dp),

                            ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Alternar perfil",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${perfilAtivo.replaceFirstChar { it.uppercase() }} - $nomeAcademiaSelecionada",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                title = { Text("Selecionar perfil") },
                                text = {
                                    Column {
                                        if (opcoes.size > 4) {
                                            CampoDeTextoPadrao(
                                                value = searchText,
                                                onValueChange = { searchText = it },
                                                label = "Buscar",
                                                placeholder = "Digite para buscar...",
                                                leadingIcon = Icons.Default.Search,
                                                onTrailingIconClick = { searchText = "" },
                                                focusRequester = focusRequester,
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                        LazyColumn {
                                            items(opcoes.filter {
                                                it.first.contains(
                                                    searchText,
                                                    ignoreCase = true
                                                )
                                            }) { (perfil, id) ->
                                                Card(
                                                    modifier = Modifier
                                                        .padding(4.dp)
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            val tipo =
                                                                if (perfil.startsWith("Prof.")) "professor" else "aluno"
                                                            SessionManager.alternarPerfil(
                                                                tipo,
                                                                id
                                                            )
                                                            showDialog = false
                                                        },
                                                    shape = RoundedCornerShape(16.dp),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = MaterialTheme.colorScheme.primary

                                                    ),
                                                    elevation = CardDefaults.cardElevation(
                                                        defaultElevation = 4.dp
                                                    )
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(8.dp)
                                                    ) {
                                                        Text(
                                                            text = perfil.replace("_", " ")
                                                                .replaceFirstChar { it.uppercase() },
                                                            color = MaterialTheme.colorScheme.onPrimary
                                                        )

                                                    }
                                                }
                                            }
                                        }
                                    }
                                },
                                confirmButton = {}
                            )
                        }
                    } else {
                        Text(
                            text = "Recursos Disponíveis",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(recursos) { recurso ->
                        RecursoCard(
                            titulo = recurso.titulo,
                            icon = recurso.icon,
                            onClick = recurso.onClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecursoCard(
    titulo: String,
    icon: RecursoIcon,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                when (icon) {
                    is RecursoIcon.Vector -> Icon(
                        imageVector = icon.icon,
                        contentDescription = titulo,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )

                    is RecursoIcon.PainterIcon -> Icon(
                        painter = icon.icon,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = titulo,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}
