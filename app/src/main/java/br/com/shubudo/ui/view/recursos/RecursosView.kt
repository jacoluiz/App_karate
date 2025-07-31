package br.com.shubudo.ui.view.recursos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Announcement
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.R
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

    LaunchedEffect(Unit) {
        viewModel.atualizarUsuarioLogado(context)
    }

    val recursos = buildList {

        add(
            RecursoItem(
                "Programação",
                RecursoIcon.Vector(Icons.Default.SportsMartialArts),
                onNavigateToProgramacao
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

        // Se o usuário for administrador
        if (usuarioLogado?.perfis?.contains("adm") == true) {
            add(
                RecursoItem(
                    "Academias",
                    RecursoIcon.PainterIcon(painterResource(R.drawable.ic_academia)),
                    onNavigateToAcademias
                )
            )

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
                    RecursoIcon.Vector(Icons.Filled.BarChart    ),
                    onNavigateToRelatorios
                )
            )
        }

        if (usuarioLogado?.perfis?.contains("dev") == true) {
            add(
                RecursoItem(
                    "Parceiros",
                    RecursoIcon.Vector(Icons.Filled.Business),
                    onNavigateToParceiros
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
                Text(
                    text = "Recursos Disponíveis",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

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
