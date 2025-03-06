package br.com.shubudo.ui.view.detalheMovimentoView.projecao

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
import br.com.shubudo.model.Projecao
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState

@Composable
fun TelaProjecaoManager(
    uiState: DetalheMovimentoUiState.Success,
    onBackNavigationClick: () -> Unit = {}
) {
    var selectedProjecao by remember { mutableStateOf<Projecao?>(null) }

    AnimatedContent(
        targetState = selectedProjecao,
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
    ) { projecao ->
        if (projecao != null) {
            TelaDetalheProjecao(
                faixa = uiState.faixa,
                projecao = projecao,
                onBackNavigationClick = { selectedProjecao = null }
            )
        } else {
            TelaListaProjecao(
                uiState = uiState,
                onBackNavigationClick = onBackNavigationClick,
                onCardClick = { selectedProjecao = it }
            )
        }
    }
}
