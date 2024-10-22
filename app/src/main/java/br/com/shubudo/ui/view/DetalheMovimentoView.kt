package br.com.shubudo.ui.view

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
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

        DetalheMovimentoUiState.Empty -> Box {
            Text("Opa, isso é um erro, seria bom reportalo :)")
        }
    }
}

@Composable
fun telaSequenciaDeCombate(
    uiState: DetalheMovimentoUiState.Success,
    onBackNavigationClick: () -> Unit
) {
    val sequenciaDeCombate = uiState.sequenciaDeCombate
    var indexSequencia by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        EsqueletoTela {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
            ) {
                // Cabeçalho ou controles acima da lista
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(top = 16.dp),
                    ) {
                        IconButton(onClick = {
                            if (indexSequencia != 0) indexSequencia-- else indexSequencia =
                                sequenciaDeCombate.size - 1
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowLeft,
                                contentDescription = "Seta para voltar",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        AnimatedContent(
                            targetState = indexSequencia,
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
                                text = "${sequenciaDeCombate[index]?.numeroOrdem?.toOrdinarioFeminino()} sequncia de combate",
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Center,
                            )
                        }

                        IconButton(onClick = {
                            if (indexSequencia < sequenciaDeCombate.size - 1) indexSequencia++ else indexSequencia =
                                0
                            Log.i(
                                "Conteudo",
                                "telaDefesaPessoal: indexDefesaExibida: $indexSequencia"
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
                }

                // Movimentos
                item {
                    sequenciaDeCombate[indexSequencia]?.movimentos?.forEachIndexed { index, movimento ->
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
                            itemDetalheMovimento(
                                descricao = "Tipo",
                                valor = movimento.tipoMovimento,
                                icone = Icons.Default.Accessibility
                            )
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

        // Botão de voltar flutuante
        botaoVoltar(
            faixa = uiState.faixa,
            listState = listState,
            onBackNavigationClick = onBackNavigationClick
        )
    }
}


@Composable
fun telaKata(uiState: DetalheMovimentoUiState.Success, onBackNavigationClick: () -> Unit) {
    var kata = uiState.kata
    var indexKataExibido by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        EsqueletoTela {
            LazyColumn(
                state = listState,
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(top = 16.dp),
                    ) {
                        IconButton(onClick = {
                            if (indexKataExibido != 0) indexKataExibido-- else indexKataExibido =
                                kata.size - 1
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowLeft,
                                contentDescription = "Seta para voltar",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        AnimatedContent(
                            targetState = indexKataExibido,
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
                                text = "${kata[index]?.ordem?.toOrdinarioFeminino()} forma",
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Center,
                                color = if (uiState.faixa == "Branca") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                )
                        }

                        IconButton(onClick = {
                            if (indexKataExibido < kata.size - 1) indexKataExibido++ else indexKataExibido =
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
                kata[indexKataExibido].movimentos.forEach { movimento ->
                    item {
                        Text(
                            modifier = Modifier.padding(top = 22.dp, start = 16.dp),
                            text = "${movimento.ordem.toOrdinario()} movimento",
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
                            itemDetalheMovimento(
                                descricao = "Tipo",
                                valor = movimento.tipoMovimento,
                                icone = Icons.Default.Accessibility
                            )
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
                            color = MaterialTheme.colorScheme.primary,
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
//                        if (movimento.observacao.isNotEmpty()) {
//                            movimento.observacao.forEach { observacao ->

//                            }
                    }
                }


                // Adicionar espaçamento no final da lista
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
        // Botão de voltar flutuante
        botaoVoltar(
            faixa = uiState.faixa,
            listState = listState,
            onBackNavigationClick = onBackNavigationClick
        )
    }
}

@Composable
fun botaoVoltar(
    faixa: String,
    listState: LazyListState,
    onBackNavigationClick: () -> Unit = {}
) {
    val isButtonVisibleState = remember {
        MutableTransitionState(true).apply { targetState = true }
    }
    var previousScrollOffset by remember { mutableStateOf(0) }

    LaunchedEffect(listState.firstVisibleItemScrollOffset) {
        if (listState.firstVisibleItemScrollOffset < previousScrollOffset) {
            // Scroll para cima
            isButtonVisibleState.targetState = true
        } else if (listState.firstVisibleItemScrollOffset > previousScrollOffset) {
            // Scroll para baixo
            isButtonVisibleState.targetState = false
        }
        previousScrollOffset = listState.firstVisibleItemScrollOffset
    }

    AnimatedVisibility(
        visibleState = isButtonVisibleState,
        modifier = Modifier.padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
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
                    tint = MaterialTheme.colorScheme.surface ,

                    )
            }
        }
    }
}

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
            faixa = uiState.faixa,
            listState = listState,
            onBackNavigationClick = onBackNavigationClick
        )
    }
}

@Composable
fun EsqueletoTela(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
        ) {
            // Remova o Column e o Modifier.verticalScroll
            content() // O content() deve incluir a LazyColumn ou qualquer outro conteúdo que precise rolar
        }
    }
}

@Composable
fun telaMovimentoPadrao(
    uiState: DetalheMovimentoUiState.Success,
    onBackNavigationClick: () -> Unit = {}
) {
    var movimento = uiState.movimento
    var indexMovimento by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        EsqueletoTela {
            AnimatedContent(
                targetState = movimento[indexMovimento],
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                        ) {
                            IconButton(onClick = {
                                if (indexMovimento != 0) indexMovimento-- else indexMovimento =
                                    movimento.size - 1
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowLeft,
                                    contentDescription = "Seta para voltar",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            AnimatedContent(
                                targetState = indexMovimento,
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
                                    text = movimento[index].nome,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.inverseSurface,
                                    textAlign = TextAlign.Center,
                                )
                            }

                            IconButton(onClick = {
                                if (indexMovimento < movimento.size - 1) indexMovimento++ else indexMovimento =
                                    0
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowRight,
                                    contentDescription = "Seta para voltar",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.weight(1f)

                                )
                            }
                        }
                    }
                    item {
                        Text(
                            text = "Detalhes",
                            color = MaterialTheme.colorScheme.secondary,
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
                            text = movimentoExibido?.descricao ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.inverseSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 0.dp, 16.dp, 8.dp),
                        )
                        if (movimentoExibido?.observacao?.isNotEmpty() == false) {
                            Text(
                                text = "Observações/Dicas",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 16.dp, 16.dp, 0.dp),
                            )
                            movimentoExibido?.observacao?.forEach { observacao ->
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
        // Botão de voltar flutuante
        botaoVoltar(
            faixa = uiState.faixa,
            listState = listState,
            onBackNavigationClick = onBackNavigationClick
        )
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
                tint = MaterialTheme.colorScheme.secondary,
            )

        } else if (iconPainter != null) {
            Icon(
                painter = iconPainter,
                contentDescription = descricao,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.secondary,
            )
        }
        Text(
            text = "$descricao: $valor",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.inverseSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(4.dp, 0.dp, 24.dp, 0.dp)
        )
    }
}