package br.com.shubudo.ui.view.detalheMovimentoView.SequenciaDeCombate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
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
import br.com.shubudo.model.SequenciaDeCombate
import br.com.shubudo.ui.components.BotaoVoltar
import br.com.shubudo.ui.components.LocalVideoPlayer
import br.com.shubudo.ui.components.itemDetalheMovimento
import br.com.shubudo.ui.view.detalheMovimentoView.EsqueletoTela
import br.com.shubudo.ui.view.detalheMovimentoView.projecao.createExoPlayer
import br.com.shubudo.utils.toOrdinario

@Composable
fun TelaDetalheSequenciaDeCombate(
    faixa: String,
    sequenciaDeCombate: SequenciaDeCombate,
    onBackNavigationClick: () -> Unit
) {
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isPlaying by remember { mutableStateOf(false) }

    // Criando o ExoPlayer corretamente
    val exoPlayer = remember(context, sequenciaDeCombate.video) {
        createExoPlayer(context, sequenciaDeCombate.video)
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                        videoPath = sequenciaDeCombate.video
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
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    // Movimentos
                    item {
                        sequenciaDeCombate.movimentos.forEachIndexed { index, movimento ->
                            Text(
                                modifier = Modifier.padding(top = 22.dp, start = 16.dp),
                                text = "${(index + 1).toOrdinario()} movimento",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 16.dp, 16.dp, 0.dp),
                            ) {
                                movimento.tipoMovimento?.let {
                                    itemDetalheMovimento(
                                        descricao = "Tipo",
                                        valor = it,
                                        icone = Icons.Default.Accessibility
                                    )
                                }
                                itemDetalheMovimento(
                                    descricao = "Base",
                                    valor = movimento.base,
                                    icone = Icons.Default.Accessibility
                                )
                            }
                            Text(
                                modifier = Modifier.padding(
                                    top = 16.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                                text = movimento.nome,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                modifier = Modifier.padding(
                                    top = 16.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                                text = movimento.descricao,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            if (movimento.observacao.isNotEmpty()) {
                                Text(
                                    modifier = Modifier.padding(
                                        top = 16.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                    text = "Observações/Dicas",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.titleSmall,
                                )
                                movimento.observacao.forEach { observacao ->
                                    Text(
                                        modifier = Modifier.padding(
                                            top = 16.dp,
                                            start = 16.dp,
                                            end = 16.dp
                                        ),
                                        text = observacao,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                            }
                        }
                    }

                    // Adicionar espaçamento no final da lista
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }

        // Botão de voltar flutuante
        BotaoVoltar(
            listState = listState,
            onBackNavigationClick = onBackNavigationClick
        )
    }
}