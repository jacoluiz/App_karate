package br.com.shubudo.ui.view.detalheMovimentoView

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.R
import br.com.shubudo.ui.components.botaoVoltar
import br.com.shubudo.ui.components.itemDetalheMovimento
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.utils.toOrdinarioFeminino
import coil.compose.AsyncImage

@Composable
fun telaDefesaPessoal(
    uiState: DetalheMovimentoUiState.Success,
    onBackNavigationClick: () -> Unit
) {
    var defesapesoal = uiState.defesaPessoal
    var indexDefesaExibida by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()
    Box(modifier = Modifier.fillMaxSize()) {
        EsqueletoTela {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                    ) {
                        IconButton(onClick = {
                            if (indexDefesaExibida != 0) indexDefesaExibida-- else indexDefesaExibida =
                                defesapesoal.size - 1
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowLeft,
                                contentDescription = "Seta para voltar",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        AnimatedContent(
                            targetState = indexDefesaExibida,
                            modifier = Modifier
                                .weight(2f),
                            transitionSpec = {
                                slideInHorizontally(
                                    initialOffsetX = { it },
                                    animationSpec = tween(durationMillis = 100)
                                ) togetherWith slideOutHorizontally(
                                    targetOffsetX = { -it },
                                    animationSpec = tween(durationMillis = 100)
                                ) using SizeTransform(clip = false)
                            },
                            label = ""
                        ) { index ->
                            Text(
                                text = defesapesoal[index].movimentos[0].nome,
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.inverseSurface
                            )
                        }

                        IconButton(onClick = {
                            if (indexDefesaExibida < defesapesoal.size - 1) indexDefesaExibida++ else indexDefesaExibida =
                                0
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowRight,
                                contentDescription = "Seta para voltar",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)

                            )
                        }
                    }
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 16.dp, 16.dp, 0.dp),
                    ) {
                        defesapesoal[indexDefesaExibida]?.let {
                            itemDetalheMovimento(
                                descricao = "Faixa",
                                valor = uiState.faixa,
                                iconPainter = painterResource(id = R.drawable.ic_faixa),
                            )

                            itemDetalheMovimento(
                                descricao = "Ordem",
                                valor = it.numeroOrdem.toOrdinarioFeminino(),
                                icone = Icons.Default.List
                            )
                        }
                    }
                    Text(
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                        text = "Passo a passo",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                        text = "1º movimento",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                        text = "O primeiro movimento de todas as defesas pessoais deve ser executado em quanto recua entrando em base de luta" +
                                "\nO primeiro movimento é ${defesapesoal[indexDefesaExibida].movimentos[0].nome}",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                    ) {
                        AsyncImage(
                            model = "https://picsum.photos/200",
                            contentDescription = "Imagem de Exemplo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(200.dp)
                                .width(250.dp)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                    Text(
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                        text = "2º movimento",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                        text = "Soco frontal na altura do rosto sem deslocar na base, esse movimento sera parado na mesma possição",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                        text = "3º movimento",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Retornar a posição inicial (base normal)",

                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Dicas",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
        }
        // Botão de voltar flutuante
        botaoVoltar(
            listState = listState,
            onBackNavigationClick = onBackNavigationClick
        )
    }
}