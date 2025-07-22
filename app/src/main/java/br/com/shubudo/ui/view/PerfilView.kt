package br.com.shubudo.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.shubudo.R
import br.com.shubudo.ui.uistate.PerfilUiState
import br.com.shubudo.ui.viewModel.ThemeViewModel
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PerfilView(
    uiState: PerfilUiState,
    themeViewModel: ThemeViewModel,
    onLogout: () -> Unit,
    onEditarPerfil: () -> Unit
) {
    when (uiState) {
        is PerfilUiState.Loading -> {
            LoadingScreen()
        }

        is PerfilUiState.Empty -> {
            LaunchedEffect("navigateToLogin") {
                onLogout()
            }
        }

        is PerfilUiState.Success -> {
            // Ajusta o tema conforme a faixa do usuário
            themeViewModel.changeThemeFaixa(uiState.corFaixa)

            PerfilContent(
                nome = uiState.nome,
                username = uiState.username,
                email = uiState.email,
                corFaixa = uiState.corFaixa,
                idade = uiState.idade,
                peso = uiState.peso,
                altura = uiState.altura,
                dan = uiState.dan,
                academia = uiState.academia,
                tamanhoFaixa = uiState.tamanhoFaixa,
                onLogout = onLogout,
                onEditarPerfil = onEditarPerfil
            )
        }

        is PerfilUiState.Error -> TODO()
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                strokeWidth = 4.dp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Carregando perfil...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun PerfilContent(
    nome: String,
    username: String,
    email: String,
    corFaixa: String,
    idade: String,
    peso: String,
    altura: String,
    dan: Int,
    academia: String,
    tamanhoFaixa: String,
    onLogout: () -> Unit,
    onEditarPerfil: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header com gradiente e foto de perfil
        ProfileHeader(
            nome = nome,
            username = username,
            corFaixa = corFaixa
        )

        // Conteúdo principal
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Cards de informações
            PersonalInfoSection(
                email = email,
                idade = idade,
                peso = peso,
                altura = altura,
                dan = dan,
                academia = academia,
                tamanhoFaixa = tamanhoFaixa,
                corFaixa = corFaixa
            )

            // Botões de ação
            ActionButtonsSection(
                onEditarPerfil = onEditarPerfil,
                onLogout = onLogout
            )
        }
    }
}

@Composable
private fun ProfileHeader(
    nome: String,
    username: String,
    corFaixa: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        // Fundo com gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    ),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        )

        // Conteúdo do header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Foto de perfil com borda
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        Color.White,
                        CircleShape
                    )
                    .padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_sequencia_de_combate),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nome e informações
            Text(
                text = nome,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "@$username",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Badge da faixa
            Surface(
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Faixa $corFaixa",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun PersonalInfoSection(
    email: String,
    idade: String,
    peso: String,
    altura: String,
    dan: Int,
    academia: String,
    tamanhoFaixa: String,
    corFaixa: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Informações Pessoais",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )

            // Grid de informações
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = email,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(
                        icon = Icons.Default.Today,
                        label = "Idade",
                        value = "${calcularIdade(idade)} anos",
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        icon = Icons.Default.MonitorWeight,
                        label = "Peso",
                        value = "$peso kg",
                        modifier = Modifier.weight(1f)
                    )
                }

                InfoCard(
                    icon = Icons.Default.Height,
                    label = "Altura",
                    value = "$altura cm",
                    modifier = Modifier.fillMaxWidth()
                )

                // Dan - só mostra para faixas Preta, Mestre ou Grão Mestre
                if (shouldShowDan(corFaixa)) {
                    InfoCard(
                        icon = painterResource(id = R.drawable.ic_faixa),
                        label = "Dan",
                        value = if (dan > 0) "${dan}º Dan" else "Sem Dan",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Academia
                InfoCard(
                    icon = painterResource(id = R.drawable.ic_sequencia_de_combate),
                    label = "Academia",
                    value = academia.ifBlank { "Não informado" },
                    modifier = Modifier.fillMaxWidth()
                )

                // Tamanho da Faixa
                InfoCard(
                    icon = painterResource(id = R.drawable.ic_faixa),
                    label = "Tamanho da Faixa",
                    value = if (tamanhoFaixa.isNotBlank()) "Tamanho $tamanhoFaixa" else "Não informado",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun InfoCard(
    icon: Any, // Pode ser ImageVector ou Painter
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (icon) {
                is ImageVector -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                is androidx.compose.ui.graphics.painter.Painter -> {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun ActionButtonsSection(
    onEditarPerfil: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Botão Editar Perfil
        Button(
            onClick = onEditarPerfil,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Editar Perfil",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    fontSize = 16.sp
                )
            }
        }

        // Botão Logout
        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Sair da Conta",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

fun calcularIdade(dataNascimento: String): Int {
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("pt", "BR"))
        val nascimento = LocalDate.parse(dataNascimento, formatter)
        val hoje = LocalDate.now()
        Period.between(nascimento, hoje).years
    } catch (e: Exception) {
        -1 // idade inválida
    }
}
