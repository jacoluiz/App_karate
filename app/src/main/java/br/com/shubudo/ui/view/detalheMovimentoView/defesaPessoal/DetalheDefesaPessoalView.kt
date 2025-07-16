package br.com.shubudo.ui.view.detalheMovimentoView.defesaPessoal

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.com.shubudo.model.DefesaPessoal
import br.com.shubudo.ui.components.BotaoVoltar
import br.com.shubudo.ui.components.LocalVideoPlayer
import br.com.shubudo.ui.view.detalheMovimentoView.EsqueletoTela
import br.com.shubudo.ui.view.detalheMovimentoView.projecao.createExoPlayer

@Composable
fun TelaDetalheDefesaPessoal(
    faixa: String,
    defesaPessoal: DefesaPessoal,
    onBackNavigationClick: () -> Unit
) {
    val listState = rememberLazyListState()
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }

    // Inicializa o ExoPlayer com o vídeo da defesa pessoal
    val exoPlayer = remember(context, defesaPessoal.video) {
        createExoPlayer(context, defesaPessoal.video)
    }

    EsqueletoTela(faixa = faixa) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Cabeçalho: Player de vídeo
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    LocalVideoPlayer(
                        exoPlayer = exoPlayer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        useController = false,
                        videoPath = defesaPessoal.video
                    )
                }
            }
            // Cabeçalho: Botões de controle (Play/Pause e Reiniciar)
            item {
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
                        Icon(
                            imageVector = Icons.Filled.Replay,
                            contentDescription = "Reiniciar",
                            tint = MaterialTheme.colorScheme.primary
                        )
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
                            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (isPlaying) "Pausar" else "Reproduzir",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            // Lista de movimentos
            items(defesaPessoal.movimentos) { movimento ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = movimento.nome,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = movimento.descricao,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    )
                }
            }
        }
    }
    // Botão de voltar
    BotaoVoltar(
        onBackNavigationClick = onBackNavigationClick,
        listState = listState
    )
}
