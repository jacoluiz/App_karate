package br.com.shubudo.ui.view.detalheMovimentoView.armamento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.exoplayer.ExoPlayer
import br.com.shubudo.model.Armamento
import br.com.shubudo.ui.components.BotaoVoltar
import br.com.shubudo.ui.components.LoadingOverlay
import br.com.shubudo.ui.components.LocalVideoPlayer
import br.com.shubudo.ui.view.detalheMovimentoView.EsqueletoTela
import br.com.shubudo.ui.viewModel.DetalheArmamentoViewModel

@Composable
fun TelaDetalheArmamento(
    viewModel: DetalheArmamentoViewModel = hiltViewModel(),
    armamento: Armamento,
    onBackNavigationClick: () -> Unit
) {
    val context = LocalContext.current

    // Instância do ExoPlayer
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    // Carrega o vídeo do armamento assim que o ViewModel ou o Armamento mudarem
    LaunchedEffect(viewModel, armamento) {
        viewModel.loadVideo(armamento, context, exoPlayer)
    }

    if (!viewModel.videoCarregado.value) {
        // Exibe um overlay de carregamento enquanto o vídeo não estiver pronto
        LoadingOverlay(isLoading = true) { }
    } else {
        EsqueletoTela(
            faixa = "Preta"
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                // Área superior: Player de vídeo
                Box(modifier = Modifier.fillMaxWidth()) {
                    LocalVideoPlayer(
                        videoPath = viewModel.currentVideoPath.value,
                        exoPlayer = exoPlayer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        useController = false
                    )
                }
                // Linha com botões: Play/Pause e Reiniciar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = {
                        if (viewModel.isPlaying.value) {
                            viewModel.pause(exoPlayer)
                        } else {
                            viewModel.play(exoPlayer)
                        }
                    }) {
                        Icon(
                            imageVector = if (viewModel.isPlaying.value) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = {
                        viewModel.seekTo(exoPlayer, 0L)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Replay,
                            contentDescription = "Reiniciar Vídeo",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Exibe o nome do armamento
                    Text(
                        text = armamento.arma,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary

                    )
                    // Exibe somente a descrição de cada movimento
                    armamento.movimentos.forEach { movimento ->
                        Text(
                            text = movimento.descricao,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                }
            }
        }
    }

    // Cria um estado de lista para ser usado no Botão de voltar
    val listState = rememberLazyListState()

    // Botão de voltar
    BotaoVoltar(
        onBackNavigationClick = onBackNavigationClick,
        listState = listState
    )
}
