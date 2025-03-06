package br.com.shubudo.ui.view.detalheMovimentoView.SequenciaDeCombate

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.shubudo.model.Movimento
import br.com.shubudo.model.SequenciaDeCombate
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.ui.view.detalheMovimentoView.MovimentosPadrao.TelaDetalheMovimentoPadrao
import br.com.shubudo.ui.view.detalheMovimentoView.MovimentosPadrao.TelaListaMovimentoPadrao

@Composable
fun TelaSequenciaDeCombateManager(
    uiState: DetalheMovimentoUiState.Success,
    onBackNavigationClick: () -> Unit = {}
){
    var selectedMoviento by remember { mutableStateOf<SequenciaDeCombate?>(null) }

    AnimatedContent(
        targetState = selectedMoviento,
        transitionSpec = {
            if (targetState == null) {
                (slideInHorizontally(initialOffsetX = { -it }) + fadeIn()).togetherWith(
                    slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                )
            } else {
                (slideInHorizontally(initialOffsetX = { it }) + fadeIn()).togetherWith(
                    slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                )
            } using SizeTransform(clip = false)
        }, label = ""
    ) { sequenciaDeCombate ->
        if (sequenciaDeCombate != null) {
            TelaDetalheSequenciaDeCombate ( faixa = uiState.faixa,

                sequenciaDeCombate = sequenciaDeCombate,
                onBackNavigationClick = { selectedMoviento = null }
            )
        } else {
            TelaListaSequenciaDeCombate (
                uiState = uiState,
                onBackNavigationClick = onBackNavigationClick,
                onCardClick = { selectedMoviento = it }
            )
        }
    }
}