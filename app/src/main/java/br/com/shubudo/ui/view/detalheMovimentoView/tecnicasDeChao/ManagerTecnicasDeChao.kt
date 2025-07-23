package br.com.shubudo.ui.view.detalheMovimentoView.tecnicasDeChao

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
import br.com.shubudo.model.TecnicaChao
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.ui.viewModel.TecnicaChaoViewModel

@Composable
fun TelaTecnicasDeChaoManager(
    uiState: DetalheMovimentoUiState.Success,
    viewModel: TecnicaChaoViewModel,
    onBackNavigationClick: () -> Unit = {}
) {
    var selectedTecnicaChao by remember { mutableStateOf<TecnicaChao?>(null) }

    AnimatedContent(
        targetState = selectedTecnicaChao,
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
    ) { tecnicaChao ->
        if (tecnicaChao != null) {
            TelaDetalheTecnicasDeChao(
                viewModel = viewModel,
                tecnicaChao = tecnicaChao,
                onBackNavigationClick = { selectedTecnicaChao = null }
            )
        } else {
            TelaListaTecnicasDeChao(
                uiState = uiState,
                onBackNavigationClick = onBackNavigationClick,
                onCardClick = { selectedTecnicaChao = it }
            )
        }
    }
}