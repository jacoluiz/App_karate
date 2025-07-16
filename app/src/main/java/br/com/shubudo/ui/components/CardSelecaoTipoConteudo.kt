package br.com.shubudo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cyclone
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    object DefesaExtraBanner : Conteudo(
        texto = "Defesas Extra Banner",
        icon = Icons.Default.Shield
    )

    object Projecao : Conteudo(
        texto = "Projeções",
    )

    object Armamento : Conteudo(
        texto = "Armamento",
    )

    object DefesasDeArma : Conteudo(
        texto = "Defesas de armas",
    )

}

@Composable
fun CardSelecaoTipoConteudo(
    programacao: Programacao,
    onNavigateToDetalheMovimento: (String, String) -> Unit,
) {
    var itens: List<Conteudo> = preencherLista(programacao)
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        itens.forEach { item ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp)),
                elevation = CardDefaults.cardElevation(4.dp),
                onClick = {
                    onNavigateToDetalheMovimento(programacao.faixa.faixa, item.texto)
                }
            ) {
                CustomIconButton(
                    texto = item.texto,
                    icon = item.icon,
                    modifier = Modifier.padding(vertical = 8.dp),
                    onClick = {
                        onNavigateToDetalheMovimento(programacao.faixa.faixa, item.texto)
                    }
                )
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
        Conteudo.SequenciaDeCombate.icon =
            ImageVector.vectorResource(id = R.drawable.ic_sequencia_de_combate)
        itensTemp.add(Conteudo.SequenciaDeCombate)
    }
    
    if (p.defesaExtraBanner.isNotEmpty()) {
        Conteudo.DefesaExtraBanner.icon =
            ImageVector.vectorResource(id = R.drawable.ic_defesa_pessoal)
        itensTemp.add(Conteudo.DefesaExtraBanner)
    }
    
    if (p.projecoes.isNotEmpty()) {
        Conteudo.Projecao.icon = ImageVector.vectorResource(id = R.drawable.ic_projecoes)
        itensTemp.add(Conteudo.Projecao)
    }

    if (p.armamento.isNotEmpty()) {
        Conteudo.Armamento.icon = ImageVector.vectorResource(id = R.drawable.ic_armamento)
        itensTemp.add(Conteudo.Armamento)
    }

    if (p.defesasDeArma.isNotEmpty()) {
        Conteudo.DefesasDeArma.icon = ImageVector.vectorResource(id = R.drawable.ic_defesas_de_armas)
        itensTemp.add(Conteudo.DefesasDeArma)
    }

    return itensTemp
}