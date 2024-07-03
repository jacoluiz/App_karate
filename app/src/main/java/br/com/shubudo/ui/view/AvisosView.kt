package br.com.shubudo.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.R

@Composable
fun AvisosContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            color = Color(0xFF8A2BE2),
            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
        ) {}
        Column {
            Card(
                modifier = Modifier
                    .padding(16.dp, 0.dp, 16.dp, 8.dp)
                    .fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Text(
                            text = "Avisos",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFF8A2BE2),
                            thickness = 2.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.em_obras),
                            contentDescription = "Em manutenção",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .size(1000.dp)
                        )
                    }
                }
            }
        }
    }

}