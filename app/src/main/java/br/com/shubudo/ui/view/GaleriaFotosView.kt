package br.com.shubudo.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.model.GaleriaFoto
import br.com.shubudo.ui.components.LoadingWrapper
import br.com.shubudo.ui.uistate.GaleriaFotosUiState
import br.com.shubudo.ui.viewModel.GaleriaFotosViewModel
import coil.compose.rememberAsyncImagePainter

@Composable
fun GaleriaFotosView(
    eventoId: String,
    onBack: () -> Unit = {},
    viewModel: GaleriaFotosViewModel = hiltViewModel()
) {
    LaunchedEffect(eventoId) {
        viewModel.carregarFotos(eventoId)
    }

    val uiState by viewModel.uiState.collectAsState()
    var selectedFotos by remember { mutableStateOf(setOf<String>()) }
    var showFullScreenImage by remember { mutableStateOf<GaleriaFoto?>(null) }
    var isSelectionMode by remember { mutableStateOf(false) }

    LoadingWrapper(isLoading = uiState is GaleriaFotosUiState.Loading) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header moderno com gradiente
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                )
                            )
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (isSelectionMode) {
                                    isSelectionMode = false
                                    selectedFotos = setOf()
                                } else {
                                    onBack()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isSelectionMode) Icons.Default.Close else Icons.Default.ArrowBack,
                                contentDescription = if (isSelectionMode) "Cancelar seleção" else "Voltar",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isSelectionMode) "Selecionando fotos" else "Galeria de Fotos",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            if (isSelectionMode) {
                                Text(
                                    text = "${selectedFotos.size} foto(s) selecionada(s)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                                )
                            }
                        }

                        if (!isSelectionMode && uiState is GaleriaFotosUiState.Success) {
                            IconButton(
                                onClick = {
                                    isSelectionMode = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SelectAll,
                                    contentDescription = "Selecionar fotos",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }

                when (uiState) {
                    is GaleriaFotosUiState.Success -> {
                        val fotos = (uiState as GaleriaFotosUiState.Success).fotos
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 160.dp),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(fotos) { foto ->
                                FotoItem(
                                    foto = foto,
                                    isSelected = selectedFotos.contains(foto._id),
                                    isSelectionMode = isSelectionMode,
                                    onFotoClick = {
                                        if (isSelectionMode) {
                                            selectedFotos = if (selectedFotos.contains(foto._id)) {
                                                selectedFotos - foto._id
                                            } else {
                                                selectedFotos + foto._id
                                            }
                                        } else {
                                            showFullScreenImage = foto
                                        }
                                    },
                                    onLongClick = {
                                        if (!isSelectionMode) {
                                            isSelectionMode = true
                                            selectedFotos = setOf(foto._id)
                                        }
                                    }
                                )
                            }
                        }
                    }

                    is GaleriaFotosUiState.Empty -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Nenhuma foto encontrada",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "As fotos do evento aparecerão aqui",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    is GaleriaFotosUiState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Erro ao carregar fotos",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = (uiState as GaleriaFotosUiState.Error).mensagem,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    else -> {}
                }
            }

            // FABs para ações quando há fotos selecionadas
            AnimatedVisibility(
                visible = isSelectionMode && selectedFotos.isNotEmpty(),
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(300)
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(300)
                ) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // FAB Download
                    SmallFloatingActionButton(
                        onClick = {
                            // TODO: Implementar download das fotos selecionadas
                        },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download fotos selecionadas"
                        )
                    }

                    // FAB Delete
                    SmallFloatingActionButton(
                        onClick = {
                            // TODO: Implementar exclusão das fotos selecionadas
                        },
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Deletar fotos selecionadas"
                        )
                    }
                }
            }
        }

        // Dialog para visualização em tela cheia
        showFullScreenImage?.let { foto ->
            FullScreenImageDialog(
                foto = foto,
                onDismiss = { showFullScreenImage = null }
            )
        }
    }
}

@Composable
private fun FotoItem(
    foto: GaleriaFoto,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onFotoClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 0.95f else 1f,
        animationSpec = tween(200),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onFotoClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = rememberAsyncImagePainter(foto.url),
                contentDescription = foto.nomeArquivo ?: "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )

            // Overlay de seleção
            if (isSelectionMode) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (isSelected)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            else
                                Color.Black.copy(alpha = 0.2f)
                        )
                )

                // Ícone de seleção
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .background(
                            Color.White.copy(alpha = 0.9f),
                            CircleShape
                        )
                        .border(
                            2.dp,
                            if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = if (isSelected) "Selecionado" else "Não selecionado",
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Gradiente sutil na parte inferior para melhor legibilidade
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.3f)
                            )
                        )
                    )
            )
        }
    }
}

@Composable
private fun FullScreenImageDialog(
    foto: GaleriaFoto,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = rememberAsyncImagePainter(foto.url),
                    contentDescription = foto.nomeArquivo ?: "",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onDismiss() }
                )

                // Botão de fechar
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(
                            Color.Black.copy(alpha = 0.5f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Informações da foto (opcional)
                foto.nomeArquivo?.let { nome ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    )
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = nome,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}