package br.com.shubudo.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material.icons.outlined.PlusOne
import androidx.compose.material.icons.outlined.SportsBar
import androidx.compose.material.icons.outlined.SportsMartialArts
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.shubudo.R
import br.com.shubudo.ui.components.DropDownMenuCard
import br.com.shubudo.ui.components.IconeFaixa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramacaoContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            color = Color(0xFF8A2BE2),
            shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
        ) {}
        Column {
            DropDownMenuCard(titulo = "Programação", icone = Icons.Outlined.SportsMartialArts) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    modifier = Modifier.padding(1.dp)
                ) {
                    DropDownMenuCard(
                        titulo = "Faixa branca",
                        iconePainter = painterResource(id = R.drawable.ic_faixa_outline),
                        tamanhoIcone = 24,
                    ) {

                    }

                    DropDownMenuCard(
                        titulo = "Faixa amarela",
                        iconePainter = painterResource(id = R.drawable.ic_faixa),
                        colorIcone = Color(0xFFE6C700)
                    ) {

                    }
                    DropDownMenuCard(
                        titulo = "Faixa laranja",
                        iconePainter = painterResource(id = R.drawable.ic_faixa),
                        colorIcone = Color(0xFFE03E00)
                    ) {

                    }
                    DropDownMenuCard(
                        titulo = "Faixa verde",
                        iconePainter = painterResource(id = R.drawable.ic_faixa),
                        colorIcone = Color(0xFF1CA600)
                    ) {

                    }
                    DropDownMenuCard(
                        titulo = "Faixa roxa",
                        iconePainter = painterResource(id = R.drawable.ic_faixa),
                        colorIcone = Color(0xFF5700A8)
                    ) {

                    }
                    DropDownMenuCard(
                        titulo = "Faixa marrom",
                        iconePainter = painterResource(id = R.drawable.ic_faixa),
                        colorIcone = Color(0xFF6F3721)
                    ) {

                    }
                }
            }
            DropDownMenuCard(titulo = "Conteudo adicional", icone = Icons.Outlined.PlusOne) {}
            DropDownMenuCard(titulo = "Dicas", icone = Icons.Outlined.TipsAndUpdates) {}
        }
    }
}