package br.com.shubudo.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.shubudo.model.Movimento

@Composable
fun SeletorMovimento(
    onMovimentoSelected: (Movimento) -> Unit = {},
    movimentos: List<Movimento>,
) {
    var destaque by remember { mutableStateOf(movimentos.firstOrNull()?.nome) }

    LazyRow(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp)
    ) {
        items(movimentos) { movimento ->
            TextButton(
                modifier = Modifier
                    .border(0.dp, Color.Transparent, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (movimento.nome == destaque) MaterialTheme.colorScheme.primary else Color.White),
                onClick = {
                    destaque = movimento.nome
                    onMovimentoSelected(movimento)
                },
            ) {
                Text(
                    text = movimento.nome,
                    color = if (movimento.nome == destaque) Color.White else Color.DarkGray,
                )
            }

        }
    }
}

@Composable
@Preview(showBackground = true)
fun SeletorMovimentoPreview() {
    SeletorMovimento(
        movimentos = listOf(
            Movimento(
                _id = "1",
                faixa = "Branca",
                tipoMovimento = "Chute",
                base = "Luta",
                nome = "Chute Frontal",
                ordem = 1,
                observacao = emptyList()
            ),
            Movimento(
                _id = "2",
                faixa = "Branca",
                tipoMovimento = "Chute",
                base = "Luta",
                nome = "Chute Semicircular",
                ordem = 2,
                observacao = listOf("observacao", "Observacao 2", "Observacao 3")
            ),
            Movimento(
                _id = "3",
                faixa = "Branca",
                tipoMovimento = "Chute",
                base = "Luta",
                nome = "Chute Lateral",
                ordem = 3,
                observacao = emptyList()
            ),
            Movimento(
                _id = "4",
                faixa = "Branca",
                tipoMovimento = "Chute",
                base = "Luta",
                nome = "Frontal Anterior Saltando",
                ordem = 4,
                observacao = emptyList()
            ),
            Movimento(
                _id = "5",
                faixa = "Branca",
                tipoMovimento = "Chute",
                base = "Luta",
                nome = "Giro Dorsal",
                ordem = 5,
                observacao = emptyList()
            ),
        )
    )
}