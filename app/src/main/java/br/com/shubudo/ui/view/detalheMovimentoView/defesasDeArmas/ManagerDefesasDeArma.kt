package br.com.shubudo.ui.view.detalheMovimentoView.defesasDeArmas

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
import br.com.shubudo.model.Armamento
import br.com.shubudo.ui.uistate.DetalheMovimentoUiState
import br.com.shubudo.ui.viewModel.DetalheArmamentoViewModel

@Composable
fun TelaDefesaDeArmaManager(
    uiState: DetalheMovimentoUiState.Success,
    viewModel: DetalheArmamentoViewModel,
    onBackNavigationClick: () -> Unit = {}
) {
    var selectedArmamento by remember { mutableStateOf<Armamento?>(null) }

    AnimatedContent(
        targetState = selectedArmamento,
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
    ) { armamento ->
        if (armamento != null) {
            TelaDetalheDefesasDeArma(
                viewModel = viewModel,
                armamento = armamento,
                onBackNavigationClick = { selectedArmamento = null }
            )
        } else {
            TelaListaDefesasDeArma(
                uiState = uiState,
                onBackNavigationClick = onBackNavigationClick,
                onCardClick = { selectedArmamento = it }
            )
        }
    }
}
