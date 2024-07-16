package br.com.shubudo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BackHand
import androidx.compose.material.icons.filled.Cyclone
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import br.com.shubudo.model.Programacao

sealed class Conteudo(
    val icone: ImageVector, val texto: String
) {
    object AtaquesDeMao : Conteudo(
        icone = Icons.Default.BackHand, texto = "Ataques de Mão"
    )

    object Chutes : Conteudo(
        icone = Icons.Default.SportsMartialArts, texto = "Chutes"
    )

    object Defesas : Conteudo(
        icone = Icons.Default.Shield, texto = "Defesas"
    )

    object DefesaPessoal : Conteudo(
        icone = Icons.Default.PersonPin, texto = "Defesa Pessoal"
    )

    object Katas : Conteudo(
        icone = Icons.Default.SelfImprovement, texto = "Katas"
    )

    object SequenciaDeCombate : Conteudo(
        icone = Icons.Default.Cyclone, texto = "Sequência de Combate"
    )

}

@Composable
fun CardSelecaoTipoConteudo(programacao: Programacao) {
    var itens: List<Conteudo> = preencherLista(programacao)
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column {
            itens.forEach() { item ->
                CustomIconButton(texto = item.texto, icon = item.icone) {
                }
            }
        }
    }
}

fun preencherLista(p: Programacao): List<Conteudo> {
    val itensTemp: MutableList<Conteudo> = mutableListOf()
    if (p.ataquesDeMao.isNotEmpty()) {
        itensTemp.add(Conteudo.AtaquesDeMao)
    }
    if (p.chutes.isNotEmpty()) {
        itensTemp.add(Conteudo.Chutes)
    }
    if (p.defesas.isNotEmpty()) {
        itensTemp.add(Conteudo.Defesas)
    }
    if (p.defesaPessoal.isNotEmpty()) {
        itensTemp.add(Conteudo.DefesaPessoal)
    }
    if (p.katas.isNotEmpty()) {
        itensTemp.add(Conteudo.Katas)
    }
    if (p.sequenciaDeCombate.isNotEmpty()) {
        itensTemp.add(Conteudo.SequenciaDeCombate)
    }
    return itensTemp
}