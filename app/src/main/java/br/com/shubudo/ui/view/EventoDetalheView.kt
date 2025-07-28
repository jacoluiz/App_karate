package br.com.shubudo.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.shubudo.SessionManager
import br.com.shubudo.model.Evento
import br.com.shubudo.ui.components.CabecalhoComIconeCentralizado
import br.com.shubudo.ui.viewModel.EventoDetalheViewModel
import br.com.shubudo.utils.toLocalDateTimeOrNull
import java.time.LocalDateTime
import kotlin.random.Random

// Data class para representar uma partícula de confete
data class ConfettiParticle(
    var x: Float,
    var y: Float,
    var velocityX: Float,
    var velocityY: Float,
    var rotation: Float,
    var rotationSpeed: Float,
    val color: Color,
    val size: Float
)

@Composable
fun ConfettiAnimation(
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val particles = remember { mutableListOf<ConfettiParticle>() }
    val animationProgress by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(3000, easing = LinearEasing),
        label = "confettiAnimation"
    )

    // Cores dos confetes
    val confettiColors = listOf(
        Color(0xFFFF6B6B), // Vermelho
        Color(0xFF4ECDC4), // Turquesa
        Color(0xFF45B7D1), // Azul
        Color(0xFF96CEB4), // Verde
        Color(0xFFFECA57), // Amarelo
        Color(0xFFFF9FF3), // Rosa
        Color(0xFF54A0FF), // Azul claro
        Color(0xFF5F27CD)  // Roxo
    )

    LaunchedEffect(isVisible) {
        if (isVisible) {
            particles.clear()
            // Criar partículas iniciais
            repeat(50) {
                particles.add(
                    ConfettiParticle(
                        x = Random.nextFloat() * 1000f,
                        y = -Random.nextFloat() * 200f,
                        velocityX = Random.nextFloat() * 4f - 2f,
                        velocityY = Random.nextFloat() * 3f + 2f,
                        rotation = Random.nextFloat() * 360f,
                        rotationSpeed = Random.nextFloat() * 10f - 5f,
                        color = confettiColors.random(),
                        size = Random.nextFloat() * 16f + 12f
                    )
                )
            }
        }
    }

    if (isVisible && animationProgress > 0f) {
        Canvas(
            modifier = modifier.fillMaxSize()
        ) {
            particles.forEach { particle ->
                // Atualizar posição da partícula
                particle.x += particle.velocityX
                particle.y += particle.velocityY
                particle.rotation += particle.rotationSpeed

                // Aplicar gravidade
                particle.velocityY += 0.1f

                // Desenhar a partícula se estiver na tela
                if (particle.x > -50 && particle.x < size.width + 50 &&
                    particle.y > -50 && particle.y < size.height + 50
                ) {

                    rotate(
                        degrees = particle.rotation,
                        pivot = Offset(particle.x, particle.y)
                    ) {
                        // Desenhar retângulo como confete
                        drawRect(
                            color = particle.color.copy(alpha = animationProgress),
                            topLeft = Offset(
                                particle.x - particle.size / 2,
                                particle.y - particle.size / 4
                            ),
                            size = androidx.compose.ui.geometry.Size(
                                particle.size,
                                particle.size / 2
                            )
                        )
                    }
                }
            }

            // Remover partículas que saíram da tela
            particles.removeAll { particle ->
                particle.y > size.height + 100
            }
        }
    }
}

@Composable
fun EventoDetalheView(
    evento: Evento,
    viewModel: EventoDetalheViewModel
) {
    val emailDoUsuario = remember { SessionManager.usuarioLogado?.email ?: "" }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var isConfirmed by remember { mutableStateOf(evento.confirmados.contains(emailDoUsuario)) }
    var showConfetti by remember { mutableStateOf(false) }
    val dataEvento = evento.dataInicio.toLocalDateTimeOrNull()
    val eventoJaPassou = dataEvento?.isBefore(LocalDateTime.now()) == true

    // Animação para o botão de confirmação
    val buttonScale by animateFloatAsState(
        targetValue = if (isConfirmed) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "buttonScale"
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Header with gradient background
            CabecalhoComIconeCentralizado(
                titulo = evento.titulo,
                subtitulo = "",
                iconeAndroid = Icons.Default.Event
            )

            // Event details card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-20).dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Date and time
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                        Column {
                            Text(
                                text = "Data e Hora",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "${evento.dataInicio.formatDate()} às ${evento.dataInicio.formatTime()}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Location
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                        Column {
                            Text(
                                text = "Local",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Text(
                                text = evento.local,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Description section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Descrição",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = evento.descricao,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (emailDoUsuario.isNotBlank() && !eventoJaPassou) {
                // Confirm presence button
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { showConfirmDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .scale(buttonScale),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isConfirmed)
                                    MaterialTheme.colorScheme.tertiary
                                else
                                    MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isConfirmed) "Presença Confirmada!" else "Confirmar Presença",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Dialog de confirmação com animações
        AnimatedVisibility(
            visible = showConfirmDialog,
            enter = fadeIn(
                animationSpec = tween(300, easing = EaseInOut)
            ) + scaleIn(
                initialScale = 0.8f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ),
            exit = fadeOut(
                animationSpec = tween(200, easing = EaseInOut)
            ) + scaleOut(
                targetScale = 0.8f,
                animationSpec = tween(200, easing = EaseInOut)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { showConfirmDialog = false },
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .padding(32.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { /* Impede que cliques no card fechem o dialog */ },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(32.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Ícone animado
                        val iconScale by animateFloatAsState(
                            targetValue = 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "iconScale"
                        )

                        Icon(
                            imageVector = if (!isConfirmed) Icons.Default.Check else Icons.Default.Clear,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(64.dp)
                                .scale(iconScale)
                        )

                        Text(
                            text = if (isConfirmed) "Cancelar Presença" else "Confirmar Presença",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = if (isConfirmed)
                                "Você deseja cancelar sua presença no evento \"${evento.titulo}\"?"
                            else
                                "Você deseja confirmar sua presença no evento \"${evento.titulo}\"?",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            TextButton(
                                onClick = { showConfirmDialog = false },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            ) {
                                Text(
                                    text = "Cancelar",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Button(
                                onClick = {
                                    viewModel.confirmarOuCancelarPresenca(
                                        evento,
                                        emailDoUsuario
                                    )
                                    isConfirmed = !isConfirmed
                                    showConfetti = isConfirmed
                                    showConfirmDialog = false
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Confirmar",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Animação de confetes
        ConfettiAnimation(
            isVisible = showConfetti,
            modifier = Modifier.fillMaxSize()
        )

        // Parar a animação de confetes após 3 segundos
        LaunchedEffect(showConfetti) {
            if (showConfetti) {
                kotlinx.coroutines.delay(3000)
                showConfetti = false
            }
        }
    }
}