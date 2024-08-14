package br.com.shubudo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.SquareFoot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.shubudo.R
import br.com.shubudo.model.Movimento

@Composable
fun MovimentoComponent(movimento: Movimento, faixa: String) {
    Column(
        modifier = Modifier.fillMaxSize()

    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.ArrowLeft,
                    contentDescription = "Icon",
                    modifier = Modifier.size(24.dp),
                )
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                ),
                modifier = Modifier
                    .weight(1f)
                    .size(250.dp)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.em_obras),
                    contentDescription = "Em manutenção",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(1000.dp)
                )
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.ArrowRight,
                    contentDescription = "Icon",
                    modifier = Modifier.size(24.dp),
                )
            }
        }
        Text(
            text = movimento.nome,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
            ),
            modifier = Modifier.padding(8.dp)
        ) {
            itemLista(
                descricao = "Tipo",
                valor = movimento.tipoMovimento,
                icone = Icons.Default.Apps,
            )
            itemLista(
                descricao = "Base",
                valor = movimento.base,
                icone = Icons.Outlined.SquareFoot,
            )
            itemLista(
                descricao = "Faixa",
                valor = faixa,
                icone = Icons.Outlined.BarChart,
            )
        }
        Text(
            text = "Dicas e observações",
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp),
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
            ),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            movimento.observacao.forEach {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
    }
}

@Composable
fun itemLista(
    descricao: String,
    valor: String,
    icone: ImageVector,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Icon(
            imageVector = icone,
            contentDescription = "Icone $descricao",
            modifier = Modifier
                .padding(8.dp, 0.dp, 8.dp, 0.dp)
        )
        Text(
            textAlign = TextAlign.Center,
            text = "$descricao: $valor",
        )
    }

}

@Composable
@Preview(showBackground = true)
fun MovimentoViewPreview() {
    MovimentoComponent(
        Movimento(
            _id = "1",
            faixa = "Branca",
            tipoMovimento = "Chute",
            base = "Luta",
            nome = "Chute Frontal",
            ordem = 1,
            observacao = listOf(
                "Observação 1", "Observação 2", "Observação 3"
            )
        ), faixa = "Branca"
    )
}