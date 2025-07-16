package br.com.shubudo.ui.view.detalheMovimentoView.SequenciaExtraBanner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.com.shubudo.model.DefesaPessoalExtraBanner
import br.com.shubudo.model.Movimento
import br.com.shubudo.ui.components.BotaoVoltar
import br.com.shubudo.ui.components.LocalVideoPlayer
import br.com.shubudo.ui.view.detalheMovimentoView.EsqueletoTela
import br.com.shubudo.ui.view.detalheMovimentoView.projecao.createExoPlayer
import br.com.shubudo.utils.toOrdinarioFeminino

@Composable
fun TelaDetalheExtraBanner(
    faixa: String,
    onBackNavigationClick: () -> Unit,
    extraBanner: DefesaPessoalExtraBanner
) {
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isPlaying by remember { mutableStateOf(false) }

    // Criando o ExoPlayer corretamente
    val exoPlayer = remember(context, extraBanner.video) {
        createExoPlayer(context, extraBanner.video)
    }
    EsqueletoTela(
        faixa = faixa
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                LocalVideoPlayer(
                    exoPlayer = exoPlayer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    useController = false,
                    videoPath = extraBanner.video
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {
                    exoPlayer.seekTo(0)
                    exoPlayer.play()
                    isPlaying = true
                }) {
                    Icon(imageVector = Icons.Default.Replay, contentDescription = "Reiniciar")
                }
                IconButton(onClick = {
                    if (isPlaying) {
                        exoPlayer.pause()
                    } else {
                        exoPlayer.play()
                    }
                    isPlaying = !isPlaying
                }) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pausar" else "Reproduzir"
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${extraBanner.numeroOrdem.toOrdinarioFeminino()} defessa pessoal extra banner",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Lista de movimentos
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(extraBanner.movimentos) { movimento ->
                        MovimentoCard(movimento = movimento)
                    }
                }

            }
        }
    }
    BotaoVoltar(
        listState = listState,
        onBackNavigationClick = onBackNavigationClick
    )

}

@Composable
fun MovimentoCard(movimento: Movimento) {

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = movimento.nome,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = movimento.descricao,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (movimento.observacao.isNotEmpty()) {
            Text(
                text = "Observações:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            movimento.observacao.forEach { obs ->
                Text(
                    text = "- $obs",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

    }
}
