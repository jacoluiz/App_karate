package br.com.shubudo.ui.view.detalheMovimentoView.SequenciaExtraBanner

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
import br.com.shubudo.model.DefesaPessoalExtraBanner
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState


@Composable
fun TelaExtraBannerManager(
    uiState: DetalheMovimentoUiState.Success,
    onBackNavigationClick: () -> Unit = {}
) {
    var selectedExtraBanner by remember { mutableStateOf<DefesaPessoalExtraBanner?>(null) }

    AnimatedContent(
        targetState = selectedExtraBanner,
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
    ) { extraBanner ->
        if (extraBanner != null) {
            TelaDetalheExtraBanner(
                extraBanner = extraBanner,
                onBackNavigationClick = { selectedExtraBanner = null }
            )
        } else {
            TelaListaExtraBanner(
                uiState = uiState,
                onBackNavigationClick = onBackNavigationClick,
                onCardClick = {
                    selectedExtraBanner = it
                }
            )
        }
    }
}