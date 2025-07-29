package br.com.shubudo.ui.view.recursos.programacao.detalheMovimento.defesasDeArmas

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
import br.com.shubudo.model.Armamento
import br.com.shubudo.ui.components.BotaoVoltar
import br.com.shubudo.ui.components.ControlesVideoPadrao
import br.com.shubudo.ui.components.LoadingOverlay
import br.com.shubudo.ui.components.OnlineVideoPlayer
import br.com.shubudo.ui.viewModel.DetalheArmamentoViewModel

@Composable
fun TelaDetalheDefesasDeArma(
    viewModel: DetalheArmamentoViewModel = hiltViewModel(),
    armamento: Armamento,
    onBackNavigationClick: () -> Unit
) {
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    LaunchedEffect(viewModel, armamento) {
        viewModel.loadVideo(armamento, exoPlayer)
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
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                )
                            ),
                        )
                        .padding(top = 48.dp, bottom = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_defesas_de_armas),
                            contentDescription = "Defesas contra armas",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = armamento.arma,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Defesas contra armas",
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
                            text = "Técnicas de defesa",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        armamento.movimentos.forEachIndexed { index, movimento ->
                            if (index > 0) {
                                Divider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }

                            Text(
                                text = movimento.descricao,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
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
