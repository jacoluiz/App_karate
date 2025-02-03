package br.com.shubudo.ui.view.detalheMovimentoView.kata

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
import br.com.shubudo.model.Kata
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.ui.viewModel.KataViewModel

@Composable
fun TelaKataManager(
    viewModel: KataViewModel,
    uiState: DetalheMovimentoUiState.Success,
    onBackNavigationClick: () -> Unit = {}
) {
    var selectedKata by remember { mutableStateOf<Kata?>(null) }

    AnimatedContent(
        targetState = selectedKata,
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
    ) { kata ->

        if (kata != null) {
            TelaDetalheKata(
                viewModel = viewModel,
                kata = kata,
                onBackNavigationClick = { selectedKata = null }
            )
        } else {
            TelaListaKata(
                uiState = uiState,
                onBackNavigationClick = onBackNavigationClick,
                onCardClick = {
                    Log.i("TelaDefesaPessoalManager", "onCardClick: $it")
                    selectedKata = it }
            )
        }
    }
}