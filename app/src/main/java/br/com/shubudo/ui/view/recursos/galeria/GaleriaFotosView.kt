package br.com.shubudo.ui.view.recursos.galeria

import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
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
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.SessionManager.usuarioLogado
import br.com.shubudo.model.GaleriaFoto
import br.com.shubudo.ui.components.LoadingDialog
import br.com.shubudo.ui.components.LoadingWrapper
import br.com.shubudo.ui.uistate.GaleriaFotosUiState
import br.com.shubudo.ui.uistate.UploadUiState
import br.com.shubudo.ui.viewModel.GaleriaFotosViewModel
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun GaleriaFotosView(
    eventoId: String,
    onBack: () -> Unit = {},
    viewModel: GaleriaFotosViewModel = hiltViewModel()
) {

    LaunchedEffect(eventoId) {
        viewModel.carregarFotos(eventoId)
    }

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var selectedFotos by remember { mutableStateOf(setOf<String>()) }
    var showFullScreenImage by remember { mutableStateOf<Int?>(null) }
    var isSelectionMode by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showUploadModal by remember { mutableStateOf(false) }
    var showDownloadLoading by remember { mutableStateOf(false) }

    val uploadState by viewModel.uploadUiState.collectAsState()

    LoadingWrapper(isLoading = uiState is GaleriaFotosUiState.Loading) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                )
                            ),
                            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
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
                                imageVector = if (isSelectionMode) Icons.Default.Close else Icons.AutoMirrored.Filled.ArrowBack,
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

                            Text(
                                text = if (isSelectionMode) "${selectedFotos.size} foto(s) selecionada(s)" else "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )

                        }

                        if (usuarioLogado?.perfis?.contains("adm") == true && !isSelectionMode) {
                            IconButton(
                                onClick = {
                                    showUploadModal = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddToPhotos,
                                    contentDescription = "Upload de fotos",
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
                                            selectedFotos =
                                                if (selectedFotos.contains(foto._id)) {
                                                    selectedFotos - foto._id
                                                } else {
                                                    selectedFotos + foto._id
                                                }
                                        } else {
                                            val fotos =
                                                (uiState as GaleriaFotosUiState.Success).fotos
                                            val index = fotos.indexOf(foto)
                                            showFullScreenImage = index
                                        }
                                    },
                                    onLongClick = {
                                        if (!isSelectionMode) {
                                            isSelectionMode = true
                                            selectedFotos = setOf(foto._id)
                                        }
                                    },
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
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                        alpha = 0.7f
                                    )
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
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                        alpha = 0.7f
                                    )
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
                            showDownloadLoading = true
                            viewModel.baixarFotosSelecionadas(
                                context = context,
                                selectedIds = selectedFotos,
                                todasFotos = (uiState as? GaleriaFotosUiState.Success)?.fotos.orEmpty(),
                                onSuccess = {
                                    showDownloadLoading = false
                                    Toast.makeText(
                                        context,
                                        "Download concluído!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    isSelectionMode = false
                                    selectedFotos = setOf()
                                },
                                onError = { erro ->
                                    showDownloadLoading = false
                                    Toast.makeText(context, erro, Toast.LENGTH_LONG)
                                        .show()
                                }
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download fotos selecionadas"
                        )
                    }
                    if (usuarioLogado?.perfis?.contains("adm") == true) {
                        // FAB Delete
                        SmallFloatingActionButton(
                            onClick = {
                                showDeleteDialog = true
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
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Confirmar Exclusão") },
                text = {
                    Column {
                        Text("Deseja realmente excluir esta(s) fotos?")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Total de ${selectedFotos.size} fotos",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Esta ação não pode ser desfeita.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deletarFotosSelecionadas(selectedFotos.toList(), eventoId)
                            showDeleteDialog = false
                            isSelectionMode = false
                            selectedFotos = setOf()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Excluir")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteDialog = false }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // Dialog para visualização em tela cheia
        showFullScreenImage?.let { currentIndex ->
            if (uiState is GaleriaFotosUiState.Success) {
                val fotos = (uiState as GaleriaFotosUiState.Success).fotos
                if (currentIndex in fotos.indices) {
                    FullScreenImageDialog(
                        fotos = fotos,
                        currentIndex = currentIndex,
                        onDismiss = { showFullScreenImage = null },
                        onIndexChange = { newIndex -> showFullScreenImage = newIndex },
                        onDelete = {
                            viewModel.deletarFoto(it, eventoId)
                        },
                        context = context,
                        viewModel = viewModel
                    )
                }
            }
        }

        // Modal de Upload
        if (showUploadModal) {
            UploadModal(
                eventoId = eventoId,
                onDismiss = { showUploadModal = false },
                viewModel = viewModel,
                uploadUiState = uploadState
            )
        }

        // Loading de Download
        if (showDownloadLoading) {
            LoadingDialog(
                textoPrincipal = "Baixando fotos selecionadas...",
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
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onFotoClick() },
                    onLongPress = {
                        onLongClick()
                    }
                )
            },
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

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
private fun FullScreenImageDialog(
    fotos: List<GaleriaFoto>,
    currentIndex: Int,
    onDelete: (String) -> Unit = {},
    onDismiss: () -> Unit,
    context: Context,
    viewModel: GaleriaFotosViewModel,
    onIndexChange: (Int) -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    var showControls by remember { mutableStateOf(true) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Auto-hide controls after 3 seconds
    LaunchedEffect(showControls) {
        if (showControls) {
            delay(3000)
            showControls = false
        }
    }

    val currentFoto = fotos[currentIndex]

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
                // Imagem principal com gestos
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    showControls = !showControls
                                }
                            )
                        }
                        .pointerInput(currentIndex) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    coroutineScope.launch {
                                        val newOffset = offsetX.value + dragAmount.x
                                        offsetX.snapTo(
                                            newOffset.coerceIn(
                                                -size.width.toFloat(),
                                                size.width.toFloat()
                                            )
                                        )
                                    }
                                },
                                onDragEnd = {
                                    val threshold = size.width * 0.3f
                                    val currentOffset = offsetX.value

                                    if (currentOffset > threshold && currentIndex > 0) {
                                        onIndexChange(currentIndex - 1)
                                    } else if (currentOffset < -threshold && currentIndex < fotos.size - 1) {
                                        onIndexChange(currentIndex + 1)
                                    }

                                    coroutineScope.launch {
                                        offsetX.animateTo(0f)
                                    }
                                }
                            )
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(currentFoto.url),
                        contentDescription = currentFoto.nomeArquivo ?: "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Controles superiores
                AnimatedVisibility(
                    visible = showControls,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
                    modifier = Modifier.align(Alignment.TopCenter)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.7f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Botão de fechar
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
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

                        // Contador de fotos
                        Text(
                            text = "${currentIndex + 1} / ${fotos.size}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .background(
                                    Color.Black.copy(alpha = 0.5f),
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        )

                        // Botões de ação
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Botão de download
                            IconButton(
                                onClick = {
                                    viewModel.baixarFotosSelecionadas(
                                        context = context,
                                        selectedIds = setOf(currentFoto._id),
                                        todasFotos = fotos,
                                        onSuccess = {
                                            Toast.makeText(
                                                context,
                                                "Imagem salva com sucesso!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        onError = { erro ->
                                            Toast.makeText(context, erro, Toast.LENGTH_LONG).show()
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Download foto",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            if (usuarioLogado?.perfis?.contains("adm") == true) {
                                // Botão de deletar
                                IconButton(
                                    onClick = {
                                        showDeleteDialog = true
                                    },
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                                            CircleShape
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Deletar foto",
                                        tint = MaterialTheme.colorScheme.onError,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Botões de navegação lateral (apenas se houver mais de uma foto)
                if (fotos.size > 1) {
                    AnimatedVisibility(
                        visible = showControls && currentIndex > 0,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut(),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(16.dp)
                    ) {
                        FloatingActionButton(
                            onClick = { onIndexChange(currentIndex - 1) },
                            modifier = Modifier.size(48.dp),
                            containerColor = Color.Black.copy(alpha = 0.6f),
                            contentColor = Color.White
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.NavigateBefore,
                                contentDescription = "Foto anterior",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = showControls && currentIndex < fotos.size - 1,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut(),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(16.dp)
                    ) {
                        FloatingActionButton(
                            onClick = { onIndexChange(currentIndex + 1) },
                            modifier = Modifier.size(48.dp),
                            containerColor = Color.Black.copy(alpha = 0.6f),
                            contentColor = Color.White
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                                contentDescription = "Próxima foto",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // Indicadores de posição (dots)
                if (fotos.size > 1) {
                    AnimatedVisibility(
                        visible = showControls,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 32.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .background(
                                    Color.Black.copy(alpha = 0.5f),
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            fotos.forEachIndexed { index, _ ->
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            if (index == currentIndex)
                                                Color.White
                                            else
                                                Color.White.copy(alpha = 0.4f),
                                            CircleShape
                                        )
                                        .clickable { onIndexChange(index) }
                                )
                            }
                        }
                    }
                }

                // Informações da foto
                currentFoto.nomeArquivo?.let { nome ->
                    AnimatedVisibility(
                        visible = showControls,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                    {
                        Box(
                            modifier = Modifier
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

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar Exclusão") },
            text = {
                Column {
                    Text("Deseja realmente excluir esta(s) fotos?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Total de 1 foto",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Esta ação não pode ser desfeita.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(currentFoto._id)
                        showDeleteDialog = false
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun UploadModal(
    eventoId: String,
    onDismiss: () -> Unit,
    viewModel: GaleriaFotosViewModel,
    uploadUiState: Map<String, UploadUiState>
) {
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            selectedImages = selectedImages + uris
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    horizontal = 16.dp,
                    vertical = 84.dp
                ),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Upload de Fotos",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botão de seleção de arquivos
                if (selectedImages.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    imagePickerLauncher.launch("image/*")
                                },
                                modifier = Modifier.size(80.dp),
                                containerColor = MaterialTheme.colorScheme.primary
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Selecionar fotos",
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Selecionar Fotos",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )

                            Text(
                                text = "Toque para escolher as fotos do seu dispositivo",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    // Lista de imagens selecionadas
                    Text(
                        text = "${selectedImages.size} foto(s) selecionada(s)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(selectedImages) { uri ->
                            SelectedImageItem(
                                uri = uri,
                                uploadUiState = uploadUiState[uri.toString()],
                                onRemove = {
                                    selectedImages = selectedImages - uri
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botão para adicionar mais fotos
                    TextButton(
                        onClick = {
                            imagePickerLauncher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Adicionar mais fotos")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botões de ação
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Voltar")
                    }

                    Button(
                        onClick = {
                            if (selectedImages.isNotEmpty()) {
                                coroutineScope.launch {
                                    val academiaId = viewModel.obterFilialIdDoUsuarioLogado()
                                    val usuarioId = usuarioLogado?._id ?: ""

                                    if (academiaId != null && usuarioId.isNotBlank()) {
                                        viewModel.uploadFotos(
                                            context = context,
                                            eventoId = eventoId,
                                            academiaId = academiaId,
                                            usuarioId = usuarioId,
                                            imageUris = selectedImages,
                                            onComplete = {
                                                selectedImages = emptyList()
                                                onDismiss()
                                            }
                                        )
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Não foi possível obter o ID da academia ou do usuário",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        },
                        enabled = selectedImages.isNotEmpty() && uploadUiState.values.none { it is UploadUiState.Uploading },
                        modifier = Modifier.weight(1f)
                    ) {
                        if (uploadUiState.values.any { it is UploadUiState.Uploading }) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text("Upload")
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectedImageItem(
    uri: Uri,
    uploadUiState: UploadUiState?,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Preview da imagem
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Informações e estado
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = uri.lastPathSegment ?: "Imagem",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                when (uploadUiState) {
                    is UploadUiState.Uploading -> {
                        LinearProgressIndicator(
                            progress = { uploadUiState.progress },
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Enviando... ${(uploadUiState.progress * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    is UploadUiState.Success -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Enviado com sucesso",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    is UploadUiState.Error -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = uploadUiState.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                maxLines = 1
                            )
                        }
                    }

                    else -> {
                        Text(
                            text = "Pronto para envio",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Botão de remover
            if (uploadUiState !is UploadUiState.Uploading) {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remover",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}