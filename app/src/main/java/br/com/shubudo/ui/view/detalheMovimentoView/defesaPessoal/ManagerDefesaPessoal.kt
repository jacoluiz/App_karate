package br.com.shubudo.ui.view.detalheMovimentoView.defesaPessoal

import android.util.Log
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
import br.com.shubudo.model.DefesaPessoal
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState

@Composable
fun TelaDefesaPessoalManager(
    uiState: DetalheMovimentoUiState.Success,
    onBackNavigationClick: () -> Unit = {}
) {
    var selectedDefesaPessoal by remember { mutableStateOf<DefesaPessoal?>(null) }

    AnimatedContent(
        targetState = selectedDefesaPessoal,
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
    ) { defesaPessoal ->

        if (defesaPessoal != null) {
            TelaDetalheDefesaPessoal(
                defesaPessoal = defesaPessoal,
                onBackNavigationClick = { selectedDefesaPessoal = null }
            )
        } else {
            TelaListaDefesaPessoal(
                uiState = uiState,
                onBackNavigationClick = onBackNavigationClick,
                onCardClick = {
                    Log.i("TelaDefesaPessoalManager", "onCardClick: $it")
                    selectedDefesaPessoal = it }
            )
        }
    }
}