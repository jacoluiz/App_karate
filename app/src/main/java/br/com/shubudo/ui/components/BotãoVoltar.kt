package br.com.shubudo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun BotaoVoltar(
    listState: LazyListState,
    onBackNavigationClick: () -> Unit = {}
) {
    val isButtonVisibleState = remember {
        MutableTransitionState(true).apply { targetState = true }
    }
    var previousScrollOffset by remember { mutableStateOf(0) }

    LaunchedEffect(listState.firstVisibleItemScrollOffset) {
        if (listState.firstVisibleItemScrollOffset < previousScrollOffset) {
            // Scroll para cima
            isButtonVisibleState.targetState = true
        } else if (listState.firstVisibleItemScrollOffset > previousScrollOffset) {
            // Scroll para baixo
            isButtonVisibleState.targetState = false
        }
        
        previousScrollOffset = listState.firstVisibleItemScrollOffset
    }

    AnimatedVisibility(
        visibleState = isButtonVisibleState,
        modifier = Modifier.padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
        ) {
            IconButton(
                onClick = {
                    onBackNavigationClick()
                }) {
                Icon(
                    modifier = Modifier.padding(0.dp),
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Seta para voltar",
                    tint = MaterialTheme.colorScheme.onPrimary,

                    )
            }
        }
    }
}