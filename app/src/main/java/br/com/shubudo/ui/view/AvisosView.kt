package br.com.shubudo.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.shubudo.ui.components.CardAviso

@Composable
fun AvisosView(
    onClickAviso: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, 16.dp),
            horizontalArrangement = Arrangement.Absolute.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyMedium,
                text = "Este é o nosso quadro de avisos. Fique atento para não perder nenhuma novidade!",
                textAlign = TextAlign.Center,
            )
        }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier
                .padding(28.dp, 0.dp, 28.dp, 8.dp)
                .fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(0.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface),
                        horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                        CardAviso(
                            imageUrl = "https://scontent.fbfh8-2.fna.fbcdn.net/v/t39.30808-6/327439149_1698359803913606_3998713113962447530_n.jpg?_nc_cat=109&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=oZGuTFgK7dUQ7kNvgGyiC8o&_nc_zt=23&_nc_ht=scontent.fbfh8-2.fna&_nc_gid=AJMqIoY6dczjw1HchZRz_p0&oh=00_AYDd_FCLf7J2cuq4_ROHTQOirDPRydRzitkmXqDs2KPyBA&oe=67501EC9",
                            title = "Pre exame, inscreva-se!",
                            description = "Se inscreva para o pre exame por dento do app",
                            onClick = onClickAviso
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