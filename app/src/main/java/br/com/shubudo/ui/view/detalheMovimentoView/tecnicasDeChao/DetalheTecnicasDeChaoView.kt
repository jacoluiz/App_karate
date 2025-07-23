package br.com.shubudo.ui.view.detalheMovimentoView.tecnicasDeChao

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.exoplayer.ExoPlayer
import br.com.shubudo.R
import br.com.shubudo.model.TecnicaChao
import br.com.shubudo.ui.components.BotaoVoltar
import br.com.shubudo.ui.components.ControlesVideoPadrao
import br.com.shubudo.ui.components.LoadingOverlay
import br.com.shubudo.ui.components.OnlineVideoPlayer
import br.com.shubudo.ui.viewModel.TecnicaChaoViewModel

@Composable
fun TelaDetalheTecnicasDeChao(
    viewModel: TecnicaChaoViewModel = hiltViewModel(),
    tecnicaChao: TecnicaChao,
    onBackNavigationClick: () -> Unit
) {
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    LaunchedEffect(tecnicaChao) {
        viewModel.loadVideoFromUrl(tecnicaChao.video, exoPlayer)
    }

    val scrollState = rememberScrollState()
    val listState = rememberLazyListState()

    if (!viewModel.videoCarregado.value) {
        LoadingOverlay(isLoading = true) {}
    } else {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Cabeçalho
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                )
                            ),
                        )
                        .padding(top = 48.dp, bottom = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_tecnicas_de_chao),
                            contentDescription = "Técnicas de chão",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = tecnicaChao.nome,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = MaterialTheme.typography.headlineMedium.lineHeight * 1.1,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Técnicas de chão",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                }

                // Vídeo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    OnlineVideoPlayer(
                        videoUrl = viewModel.currentVideoUrl.value,
                        exoPlayer = exoPlayer,
                        modifier = Modifier.fillMaxSize(),
                        useController = false
                    )
                }

                // Controles
                ControlesVideoPadrao(
                    exoPlayer = exoPlayer,
                    isPlaying = viewModel.isPlaying.value,
                    onPlayingChange = { viewModel.setIsPlaying(it) }
                )

                // Detalhes dos movimentos
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
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Descrição da Técnica",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = tecnicaChao.descricao,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (tecnicaChao.observacao.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Observações",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Column {
                                tecnicaChao.observacao.forEach { obs ->
                                    Text(
                                        text = "• $obs",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    BotaoVoltar(
        onBackNavigationClick = onBackNavigationClick,
        listState = listState
    )
}
