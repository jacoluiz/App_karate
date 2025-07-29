package br.com.shubudo.ui.view.recursos.programacao.detalheMovimento.MovimentosPadrao

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
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState


@Composable
fun TelaMovimentoPadraoManager(
    uiState: DetalheMovimentoUiState.Success,
    onBackNavigationClick: () -> Unit = {}
) {
    var selectedMovimento by remember { mutableStateOf<Movimento?>(null) }

    AnimatedContent(
        targetState = selectedMovimento,
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
    ) { movimento ->
        if (movimento != null) {
            TelaDetalheMovimentoPadrao(
                faixa = uiState.faixa,
                movimento = movimento,
                onBackNavigationClick = { selectedMovimento = null }
            )
        } else {
            TelaListaMovimentoPadrao(
                uiState = uiState,
                onBackNavigationClick = onBackNavigationClick,
                onCardClick = { selectedMovimento = it }
            )
        }
    }
}