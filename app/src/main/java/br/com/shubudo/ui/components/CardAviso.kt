package br.com.shubudo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.shubudo.model.Aviso
import coil.compose.rememberAsyncImagePainter

@Composable
fun CardAviso(
    aviso: Aviso,
    modifier: Modifier = Modifier,
    onClick: (Aviso) -> Unit
) {
    // URL padrão caso o aviso não possua imagem
    val imagemDefault = "https://karateshubudo.com.br/wp-content/uploads/elementor/thumbs/LOGO_KARATE-JPG-2010-01-01-pgd3sk0v62xv53x0bhlsbq53k0unw3zph03j682rww.png"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = MaterialTheme.shapes.medium,
        // Envolve a chamada onClick em uma lambda que passa o objeto 'aviso'
        onClick = { onClick(aviso) },
        colors = CardDefaults.cardColors()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Exibe a imagem (usa a imagem padrão se necessário)
            Image(
                painter = rememberAsyncImagePainter(
                    model = if (aviso.imagem.isNullOrBlank()) imagemDefault else aviso.imagem
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Exibe o título e o conteúdo do aviso
            Column {
                Text(
                    text = aviso.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = aviso.conteudo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
