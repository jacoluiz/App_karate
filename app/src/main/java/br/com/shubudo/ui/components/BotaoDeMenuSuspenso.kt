package br.com.shubudo.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.shubudo.model.Movimento
import br.com.shubudo.utils.toOrdinario
import br.com.shubudo.utils.toOrdinarioFeminino

@Composable
fun ItemBotaoDeMenuSuspenso(
    movimento: Movimento,
    selecionado: Boolean = false,
    clickNoBotao: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, 4.dp)
            .background(
                if (selecionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.8f
                ),
                shape = CircleShape
            )
            .wrapContentWidth()
            .clickable {
                clickNoBotao()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(MaterialTheme.colorScheme.secondary, shape = CircleShape)
        )
        Text(
            text = "${if (movimento.tipoMovimento == "Defesa") movimento.ordem.toOrdinarioFeminino() else movimento.ordem.toOrdinario()} ${movimento.tipoMovimento}",
            maxLines = 1,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                .height(30.dp)
                .wrapContentHeight(),


            )
    }
}

@Composable
fun BotaoDeMenuSuspenso(
    movimentos: List<Movimento>,
    selecionado: String,
    onMovimentoSelected: (Movimento) -> Unit,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit
) {
    AnimatedContent(
        targetState = expanded,
        transitionSpec = {
            fadeIn(animationSpec = tween(600)) togetherWith fadeOut(animationSpec = tween(300))
        },
        label = "Animação de Conteudo"
    ) { targetExpanded ->
        if (targetExpanded) {
            Column {
                movimentos.forEach { movimento ->
                    ItemBotaoDeMenuSuspenso(
                        movimento = movimento,
                        selecionado = movimento.nome == selecionado,
                        clickNoBotao = {
                            onMovimentoSelected(movimento)
                        }
                    )
                }
            }
        } else {
            val movimentoSelecionado = movimentos.firstOrNull { it.nome == selecionado }
            ItemBotaoDeMenuSuspenso(
                movimento = movimentoSelecionado ?: movimentos.first(),
                selecionado = true,
                clickNoBotao = {
                    onExpandChange(true)
                }
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun BotaoDeMenuSuspensoPreview() {
    BotaoDeMenuSuspenso(
        movimentos = listOf(
            Movimento(
                _id = "1",
                faixa = "Branca",
                tipoMovimento = "Chute",
                base = "Luta",
                nome = "Chute Frontal",
                ordem = 1,
                observacao = listOf("Observação 1", "Observação 2"),
            ),
            Movimento(
                _id = "2",
                faixa = "Branca",
                tipoMovimento = "Chute",
                base = "Luta",
                nome = "Chute semicircular",
                ordem = 2,
                observacao = listOf("Observação 1", "Observação 2"),
            ),
            Movimento(
                _id = "3",
                faixa = "Branca",
                tipoMovimento = "Chute",
                base = "Luta",
                nome = "Chute lateral",
                ordem = 3,
                observacao = listOf("Observação 1", "Observação 2"),
            ),
            Movimento(
                _id = "4",
                faixa = "Branca",
                tipoMovimento = "Chute",
                base = "Luta",
                nome = "Chute frontal anterior saltando",
                ordem = 4,
                observacao = listOf("Observação 1", "Observação 2"),
            ),
        ),
        selecionado = "Chute Frontal",
        onMovimentoSelected = {},
        expanded = true,
        onExpandChange = {},
    )
}