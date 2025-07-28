package br.com.shubudo.ui.view.detalheMovimentoView.SequenciaDeCombate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.shubudo.R
import br.com.shubudo.model.SequenciaDeCombate
import br.com.shubudo.ui.components.ControlesVideoPadrao
import br.com.shubudo.ui.components.OnlineVideoPlayer
import br.com.shubudo.ui.view.detalheMovimentoView.projecao.createExoPlayer
import br.com.shubudo.utils.toOrdinario

@Composable
fun TelaDetalheSequenciaDeCombate(
    sequenciaDeCombate: SequenciaDeCombate,
    onBackNavigationClick: () -> Unit
) {
    rememberLazyListState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isPlaying by remember { mutableStateOf(true) }

    val exoPlayer = remember(context, sequenciaDeCombate.video) {
        createExoPlayer(context, sequenciaDeCombate.video)
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                            )
                        ),
                    )
                    .padding(top = 48.dp, bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onBackNavigationClick,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .size(36.dp)
                        .background(Color(0x30FFFFFF), RoundedCornerShape(18.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sequencia_de_combate),
                        contentDescription = "Sequência de Combate",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Sequência de Combate",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                OnlineVideoPlayer(
                    exoPlayer = exoPlayer,
                    modifier = Modifier.fillMaxSize(),
                    useController = false,
                    videoUrl = sequenciaDeCombate.video
                )
            }

            // Controles de vídeo (refatorado com componente reutilizável)
            ControlesVideoPadrao(
                exoPlayer = exoPlayer,
                isPlaying = isPlaying,
                onPlayingChange = { isPlaying = it }
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    sequenciaDeCombate.movimentos.forEachIndexed { index, movimento ->
                        if (index > 0) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 16.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${(index + 1).toOrdinario()} Movimento",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = movimento.nome,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = movimento.descricao,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (movimento.observacao.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Observações:",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    movimento.observacao.forEach { observacao ->
                                        Row(verticalAlignment = Alignment.Top) {
                                            Text(
                                                text = "•",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.padding(end = 8.dp, top = 2.dp)
                                            )
                                            Text(
                                                text = observacao,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
