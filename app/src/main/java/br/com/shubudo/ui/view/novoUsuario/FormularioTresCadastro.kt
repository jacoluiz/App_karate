package br.com.shubudo.ui.view.novoUsuario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import br.com.shubudo.R
import br.com.shubudo.ui.viewModel.NovoUsuarioViewModel

@Composable
fun PaginaTresCadastro(
    novoUsuarioViewModel: NovoUsuarioViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header
        Column {
            Text(
                text = "Confirmação dos Dados",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Revise suas informações antes de finalizar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Resumo Completo
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Resumo Completo",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryItem("Nome", novoUsuarioViewModel.nome)
                    SummaryItem("Data de nascimento", novoUsuarioViewModel.idade)
                    SummaryItem("Faixa", novoUsuarioViewModel.faixa)

                    if (shouldShowDan(novoUsuarioViewModel.faixa)) {
                        val danText = if (novoUsuarioViewModel.dan == 0) "Sem Dan" else "${novoUsuarioViewModel.dan}º Dan"
                        SummaryItem("Dan", danText)
                    }

                    SummaryItem("Academia", novoUsuarioViewModel.academia.ifBlank { "Não informado" })
                    SummaryItem("E-mail", novoUsuarioViewModel.email)
                    SummaryItem("Altura", if (novoUsuarioViewModel.altura.isNotBlank()) "${novoUsuarioViewModel.altura} cm" else "")
                    SummaryItem("Peso", if (novoUsuarioViewModel.peso.isNotBlank()) "${novoUsuarioViewModel.peso} kg" else "")
                    SummaryItem("Tamanho da Faixa", novoUsuarioViewModel.tamanhoFaixa.ifBlank { "Não informado" })
                }
            }
        }

        // Mensagem de confirmação
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = "✅ Revise os dados acima e clique em 'Finalizar' para concluir seu cadastro",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SummaryItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value.ifBlank { "-" },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 4.dp),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    )
}