package br.com.shubudo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cyclone
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import br.com.shubudo.R
import br.com.shubudo.model.Programacao

sealed class Conteudo(
    val texto: String,
    var icon: ImageVector? = null
) {
    object AtaquesDeMao : Conteudo(
        texto = "Ataques de Mão"
    )

    object Chutes : Conteudo(
        texto = "Chutes",
        icon = Icons.Default.SportsMartialArts
    )

    object Defesas : Conteudo(
        texto = "Defesas",
        icon = Icons.Default.Shield
    )

    object DefesaPessoal : Conteudo(
        texto = "Defesa Pessoal",
        icon = Icons.Default.PersonPin
    )

    object Katas : Conteudo(
        texto = "Katas",
        icon = Icons.Default.SelfImprovement
    )

    object SequenciaDeCombate : Conteudo(
        texto = "Sequência de Combate",
        icon = Icons.Default.Cyclone
    )

}

@Composable
fun CardSelecaoTipoConteudo(
    programacao: Programacao,
    onNavigateToDetalheMovimento: (String, String) -> Unit
) {
    var itens: List<Conteudo> = preencherLista(programacao)
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
    ) {
        Column {
            itens.forEach() { item ->
                Row(
                    modifier = Modifier
                        .padding(top = 2.dp, bottom = 2.dp),
                ) {
                    CustomIconButton(
                        texto = item.texto,
                        icon = item.icon,
                        onClick = {
                            onNavigateToDetalheMovimento(programacao.faixa.faixa, item.texto)
                        }
                    )
                }

            }
        }
    }
}

@Composable
fun preencherLista(p: Programacao): List<Conteudo> {
    val itensTemp: MutableList<Conteudo> = mutableListOf()
    if (p.ataquesDeMao.isNotEmpty()) {
        Conteudo.AtaquesDeMao.icon = ImageVector.vectorResource(id = R.drawable.ic_ataque_de_mao)
        itensTemp.add(Conteudo.AtaquesDeMao)
    }
    if (p.chutes.isNotEmpty()) {
        itensTemp.add(Conteudo.Chutes)
    }
    if (p.defesas.isNotEmpty()) {
        itensTemp.add(Conteudo.Defesas)
    }
    if (p.defesaPessoal.isNotEmpty()) {
        Conteudo.DefesaPessoal.icon = ImageVector.vectorResource(id = R.drawable.ic_defesa_pessoal)
        itensTemp.add(Conteudo.DefesaPessoal)
    }
    if (p.katas.isNotEmpty()) {
        itensTemp.add(Conteudo.Katas)
    }
    if (p.sequenciaDeCombate.isNotEmpty()) {
        Conteudo.SequenciaDeCombate.icon = ImageVector.vectorResource(id = R.drawable.ic_sequencia_de_combate)
        itensTemp.add(Conteudo.SequenciaDeCombate)
    }

    return itensTemp
}