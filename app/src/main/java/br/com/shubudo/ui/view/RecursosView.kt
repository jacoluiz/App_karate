package br.com.shubudo.ui.view

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Announcement
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.R
import br.com.shubudo.SessionManager.usuarioLogado

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
    onNavigateToBaseUsuarios: () -> Unit = {}
) {
    val recursos = buildList {

        add(RecursoItem("Eventos", RecursoIcon.Vector(Icons.Default.Event), onNavigateToEventos))
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
        }
    }.sortedBy { it.titulo }


    Column(modifier = Modifier.fillMaxSize()) {
        // Cabeçalho com fundo gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                        )
                    ),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.LibraryBooks,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Recursos do Karate",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Acesse materiais complementares e informações importantes",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

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
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                when (icon) {
                    is RecursoIcon.Vector -> Icon(
                        imageVector = icon.icon,
                        contentDescription = titulo,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(32.dp)
                    )

                    is RecursoIcon.PainterIcon -> Icon(
                        painter = icon.icon,
                        tint = MaterialTheme.colorScheme.onSurface,
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
