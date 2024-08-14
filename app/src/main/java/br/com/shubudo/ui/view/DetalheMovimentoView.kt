package br.com.shubudo.ui.view

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.shubudo.R
import br.com.shubudo.model.DefesaPessoal
import br.com.shubudo.model.Movimento
import br.com.shubudo.ui.components.BotaoDeMenuSuspenso
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.utils.toOrdinario
import br.com.shubudo.utils.toOrdinarioFeminino
import coil.compose.AsyncImage

@Composable
fun DetalheMovimentoView(
    uiState: DetalheMovimentoUiState, onBackNavigationClick: () -> Unit = {}
) {

    when (uiState) {
        is DetalheMovimentoUiState.Loading -> {
            Text("Loading...")
        }

        is DetalheMovimentoUiState.Success -> {
            if (uiState.movimento.isNotEmpty()) {
                telaMovimentoPadrao(uiState, onBackNavigationClick)
            } else if (uiState.defesaPessoal.isNotEmpty()) {
                telaDefesaPessoal(uiState, onBackNavigationClick)
            } else if (uiState.kata.isNotEmpty()) {
                telaKata(uiState, onBackNavigationClick)
            } else if (uiState.sequenciaDeCombate.isNotEmpty()) {
                telaSequenciaDeCombate(uiState, onBackNavigationClick)
            }
        }

        DetalheMovimentoUiState.Empty -> TODO()
    }
}

@Composable
fun telaSequenciaDeCombate(
    uiState: DetalheMovimentoUiState.Success,
    onBackNavigationClick: () -> Unit
) {
}

@Composable
fun telaKata(uiState: DetalheMovimentoUiState.Success, onBackNavigationClick: () -> Unit) {

}

@Composable
fun telaDefesaPessoal(uiState: DetalheMovimentoUiState.Success, onBackNavigationClick: () -> Unit) {
    var defesapesoal = uiState.defesaPessoal
    var indexDefesaExibida by remember { mutableIntStateOf(0) }
    Box(modifier = Modifier.fillMaxSize()) {
        EsqueletoTela {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
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
                        )
                    }

                    IconButton(onClick = {
                        if (indexDefesaExibida < defesapesoal.size - 1) indexDefesaExibida++ else indexDefesaExibida =
                            0
                        Log.i(
                            "Conteudo",
                            "telaDefesaPessoal: indexDefesaExibida: $indexDefesaExibida"
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowRight,
                            contentDescription = "Seta para voltar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)

                        )
                    }
                }
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
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                shape = CircleShape
            )
        ) {
            IconButton(
                onClick = {
                    onBackNavigationClick()
                }) {
                Icon(
                    modifier = Modifier.padding(0.dp),
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Seta para voltar",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
fun EsqueletoTela(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(
            model = "https://picsum.photos/200",
            contentDescription = "Imagem de Exemplo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
        )
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 180.dp),
                color = MaterialTheme.colorScheme.inverseOnSurface,
                shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun telaMovimentoPadrao(
    uiState: DetalheMovimentoUiState.Success,
    onBackNavigationClick: () -> Unit = {}
) {
    var movimentos = uiState.movimento
    var movimentoExibido by remember { mutableStateOf(movimentos.firstOrNull()) }
    var expanded by remember { mutableStateOf(false) }
    var visibleState by remember { mutableStateOf(true) }
    Box(modifier = Modifier.fillMaxSize()) {
        EsqueletoTela {
            AnimatedContent(
                targetState = movimentoExibido,
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
                Column {
                    Text(
                        text = "${movimentoExibido?.nome}",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 32.dp, 16.dp, 0.dp),
                    )
                    Text(
                        text = "Detalhes",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp, 16.dp, 52.dp),
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp, 16.dp, 0.dp),
                    ) {
                        movimentoExibido?.let {
                            itemDetalheMovimento(
                                descricao = "Tipo",
                                valor = it.tipoMovimento,
                                icone = Icons.Default.Apps
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            itemDetalheMovimento(
                                descricao = "Base",
                                valor = it.base,
                                icone = Icons.Default.Accessibility
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp, 16.dp, 52.dp),
                    ) {
                        movimentoExibido?.let {
                            itemDetalheMovimento(
                                descricao = "Faixa",
                                valor = uiState.faixa,
                                iconPainter = painterResource(id = R.drawable.ic_faixa),

                                )
                            Spacer(modifier = Modifier.width(8.dp))
                            itemDetalheMovimento(
                                descricao = "Ordem",
                                valor = if (it.tipoMovimento == "Defesa") it.ordem.toOrdinarioFeminino() else it.ordem.toOrdinario(),
                                icone = Icons.Default.List
                            )
                        }
                    }
                    Text(
                        text = "Dicas",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 16.dp, 16.dp, 0.dp),
                    )
                    movimentoExibido?.let {
                        it.observacao.forEach { observacao ->
                            Text(
                                text = observacao,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 0.dp, 16.dp, 8.dp),
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                shape = CircleShape
            )
        ) {
            IconButton(
                onClick = {
                    onBackNavigationClick()
                }) {
                Icon(
                    modifier = Modifier.padding(0.dp),
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Seta para voltar",
                    tint = MaterialTheme.colorScheme.outline,
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = 180.dp - 15.dp)
                .padding(start = 26.dp)
        ) {
            BotaoDeMenuSuspenso(movimentos = movimentos,
                selecionado = movimentoExibido?.nome ?: "",
                onMovimentoSelected = { movimento ->
                    movimentoExibido = movimento
                    expanded = false
                    visibleState = false
                    visibleState = true
                },
                expanded = expanded,
                onExpandChange = { expanded = it })
        }
    }
}

@Composable
fun itemDetalheMovimento(
    descricao: String,
    valor: String,
    icone: ImageVector? = null,
    iconPainter: Painter? = null,
) {
    val icone = when (valor) {
        "Chute" -> Icons.Default.SportsMartialArts
        "Ataque de mão" -> Icons.Default.FrontHand
        "Defesa" -> Icons.Default.Security
        else -> icone
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        if (icone != null) {
            Icon(
                imageVector = icone,
                contentDescription = descricao,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary,
            )

        } else if (iconPainter != null) {
            Icon(
                painter = iconPainter,
                contentDescription = descricao,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Text(
            text = "$descricao: $valor",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(4.dp, 0.dp, 24.dp, 0.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DetalheMovimentoViewPreview() {
    DetalheMovimentoView(
        DetalheMovimentoUiState.Success(
            movimento = emptyList(),
            defesaPessoal = listOf(
                DefesaPessoal(
                    faixa = "Branca",
                    _id = "1",
                    numeroOrdem = 1,
                    movimentos = listOf(
                        Movimento(
                            _id = "1", faixa = "Branca",
                            nome = "DEFESA SUPERIOR NO ROSTO (MÃO FECHADA)",
                            tipoMovimento = "Defesa",
                            base = "Frente",
                            ordem = 1,
                            observacao = listOf("Observação 1", "Observação 2")
                        ),
                        Movimento(
                            _id = "2", faixa = "Branca",
                            nome = "Defesa 2",
                            tipoMovimento = "Defesa",
                            base = "Frente",
                            ordem = 2,
                            observacao = listOf("Observação 1", "Observação 2")
                        ),
                        Movimento(
                            _id = "3",
                            faixa = "Branca",
                            nome = "Defesa 3",
                            tipoMovimento = "Defesa",
                            base = "Frente",
                            ordem = 3,
                            observacao = listOf("Observação 1", "Observação 2")
                        )
                    )
                )
            ),
            kata = emptyList(),
            sequenciaDeCombate = emptyList(),
            faixa = "Branca",
        )
    )
}