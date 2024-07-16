package br.com.shubudo.ui.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.shubudo.ui.components.CardSelecaoTipoConteudo
import br.com.shubudo.ui.uistate.DetalheFaixaUiState

@Composable
fun DetalheFaixaView(
    uiState: DetalheFaixaUiState,
) {
    when (uiState) {
        is DetalheFaixaUiState.Loading -> {

        }

        is DetalheFaixaUiState.Success -> {
            val programacao = uiState.programacao
            Box(modifier = Modifier.fillMaxSize()) {
                CardSelecaoTipoConteudo(programacao)
            }
        }

        DetalheFaixaUiState.Empty -> TODO()
    }
}

@Composable
@Preview
fun DetalheFaixaViewPreview() {

}