package br.com.shubudo.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import br.com.shubudo.ui.view.LoadingScreen

/**
 * Wrapper que pode ser usado para envolver qualquer tela com loading
 */
@Composable
fun LoadingWrapper(
    isLoading: Boolean,
    loadingText: String = "Carregando...",
    backgroundColor: Color = androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Conteúdo da tela principal
        content()

        // Tela de loading sobreposta
        LoadingScreen(
            isVisible = isLoading,
            loadingText = loadingText,
            backgroundColor = backgroundColor
        )
    }
}