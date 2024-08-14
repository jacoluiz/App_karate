package br.com.shubudo.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AvisosView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Card(
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.inverseOnSurface,
            ),
            modifier = Modifier
                .padding(16.dp, 0.dp, 16.dp, 8.dp)
                .fillMaxSize(),

            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(16.dp),
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
                        color = MaterialTheme.colorScheme.primary,
                        thickness = 2.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Você está avisado ;)",
                            color = Color.Gray,
                        )
                    }
                }
            }
        }
    }

}

@Composable
@Preview
fun AvisosContentPreview() {
    AvisosView()
}