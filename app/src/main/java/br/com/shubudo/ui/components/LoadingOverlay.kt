package br.com.shubudo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingOverlay(isLoading: Boolean, content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Conteúdo da tela principal
        content()

        // Se isLoading for verdadeiro, mostra a tela de loading
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                // Exemplo de animação: pode substituir por qualquer animação personalizada
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp),
                    color = Color.White
                )

                // Aqui você pode adicionar sua animação personalizada com bonequinho
                // ou qualquer outra que deseja
            }
        }
    }
}
