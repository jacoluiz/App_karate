package br.com.shubudo.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetalheAvisoView(
    titulo: String,
    conteudo: String
) {
    Card(
        modifier = Modifier
            .padding(16.dp, 16.dp, 16.dp, 16.dp)
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titulo,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = conteudo,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyLarge,
            )
        }

    }
}