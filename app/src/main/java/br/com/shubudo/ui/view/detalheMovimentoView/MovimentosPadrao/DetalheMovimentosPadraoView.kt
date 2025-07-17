package br.com.shubudo.ui.view.detalheMovimentoView.MovimentosPadrao

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Apps
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.model.Movimento
import br.com.shubudo.ui.components.BotaoVoltar
import br.com.shubudo.ui.components.LocalVideoPlayer
import br.com.shubudo.ui.components.itemDetalheMovimento
import br.com.shubudo.ui.view.detalheMovimentoView.EsqueletoTela
import br.com.shubudo.ui.view.detalheMovimentoView.projecao.createExoPlayer


@Composable
fun TelaDetalheMovimentoPadrao(
    faixa: String,
    movimento: Movimento,
    onBackNavigationClick: () -> Unit
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    var isPlaying by remember { mutableStateOf(false) }

    // Criando o ExoPlayer corretamente
    val exoPlayer = remember(context, movimento.video) {
        movimento.video?.let { createExoPlayer(context, it) }
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
                    if (exoPlayer != null) {
                        LocalVideoPlayer(
                            exoPlayer = exoPlayer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            useController = false,
                            videoPath = movimento.video
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = {
                        exoPlayer?.seekTo(0)
                        exoPlayer?.play()
                        isPlaying = true
                    }) {
                        Icon(imageVector = Icons.Default.Replay, contentDescription = "Reiniciar")
                    }
                    IconButton(onClick = {
                        if (isPlaying) {
                            if (exoPlayer != null) {
                                exoPlayer.pause()
                            }
                        } else {
                            if (exoPlayer != null) {
                                exoPlayer.play()
                            }
                        }
                        isPlaying = !isPlaying
                    }) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pausar" else "Reproduzir"
                        )
                    }
                }
                AnimatedContent(
                    targetState = movimento,
                    transitionSpec = {
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(durationMillis = 600)
                        ) togetherWith slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(durationMillis = 600)
                        ) using SizeTransform(clip = false)
                    },
                    label = "Animação de Conteudo"
                ) { movimentoExibido ->
                    LazyColumn(
                        state = listState,
                    ) {

                        item {
                            Text(
                                text = movimento.nome,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 16.dp, 16.dp, 16.dp),
                            )
                            Text(
                                text = "Detalhes",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 16.dp, 16.dp, 16.dp),
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 0.dp, 16.dp, 0.dp),
                            ) {
                                movimentoExibido.let {
                                    it.tipoMovimento?.let { it1 ->
                                        itemDetalheMovimento(
                                            descricao = "Tipo",
                                            valor = it1,
                                            icone = Icons.Default.Apps
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    itemDetalheMovimento(
                                        descricao = "Base",
                                        valor = it.base,
                                        icone = Icons.Default.Accessibility
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = movimentoExibido.descricao,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.inverseSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 0.dp, 16.dp, 8.dp),
                            )
                            if (movimentoExibido.observacao.isNotEmpty()) {
                                Text(
                                    text = "Observações/Dicas",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp, 16.dp, 16.dp, 0.dp),
                                )
                                movimentoExibido.observacao.forEach { observacao ->
                                    Text(
                                        text = observacao,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.inverseSurface,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp, 0.dp, 16.dp, 8.dp),
                                    )
                                }
                            }
                        }
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
