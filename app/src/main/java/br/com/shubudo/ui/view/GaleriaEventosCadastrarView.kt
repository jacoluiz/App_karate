package br.com.shubudo.ui.view

import CampoDeTextoPadrao
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.SessionManager.usuarioLogado
import br.com.shubudo.ui.view.novoUsuario.ModernTextFieldWithDateMask
import br.com.shubudo.ui.viewModel.GaleriaEventosViewModel
import br.com.shubudo.utils.aplicarMascaraDeDataParaAniversario
import br.com.shubudo.utils.dataNaoNoFuturo
import br.com.shubudo.utils.formatarDataHoraLocal

@Composable
fun GaleriaEventosCadastrarView(
    eventoId: String? = null,
    navigationBack: () -> Unit,
    viewModel: GaleriaEventosViewModel = hiltViewModel()
) {
    val eventoSelecionado by viewModel.eventoSelecionado.collectAsState()
    val focusManager = LocalFocusManager.current

    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var preenchido by remember { mutableStateOf(false) }
    var data by remember { mutableStateOf(TextFieldValue(viewModel.data)) }

    val focusRequesterTitulo = remember { FocusRequester() }
    val focusRequesterDescricao = remember { FocusRequester() }

    LaunchedEffect(eventoId) {
        eventoId?.let { viewModel.carregarEvento(it) }
    }

    LaunchedEffect(eventoSelecionado, preenchido) {
        if (eventoSelecionado != null && !preenchido) {
            titulo = eventoSelecionado?.titulo.orEmpty()
            descricao = eventoSelecionado?.descricao.orEmpty()
            data = TextFieldValue(formatarDataHoraLocal(eventoSelecionado?.data ?: "", false))
            preenchido = true
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Cabeçalho
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                        )
                    ),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = navigationBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = if (eventoId != null) "Editar Evento" else "Novo Evento",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "Preencha os detalhes do evento",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Cartão de conteúdo
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-20).dp),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Título
                CampoDeTextoPadrao(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = "Título *",
                    placeholder = "Nome do evento",
                    leadingIcon = Icons.Default.Title,
                    focusRequester = remember { FocusRequester() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Descrição
                CampoDeTextoPadrao(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = "Descrição",
                    placeholder = "Descrição do evento (opcional)",
                    leadingIcon = Icons.Default.Description,
                    focusRequester = remember { FocusRequester() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Data
                ModernTextFieldWithDateMask(
                    value = data,
                    onValueChange = { newValue ->
                        data = aplicarMascaraDeDataParaAniversario(newValue)
                        viewModel.data = data.text
                    },
                    label = "Data do evento",
                    placeholder = "dd/mm/aaaa",
                    leadingIcon = Icons.Default.DateRange,
                    focusRequester = focusRequesterDescricao,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { }
                    ),
                    helperText = "Formato: 25/12/2024",
                    isError = data.text.isNotEmpty() && !dataNaoNoFuturo(data.text)
                )


                Spacer(modifier = Modifier.height(24.dp))

                // Botão Salvar
                Button(
                    onClick = {
                        viewModel.salvarEvento(
                            onSalvo = navigationBack,
                            id = eventoSelecionado?._id,
                            criadoPor = usuarioLogado?._id ?: "",
                            nomeFilial = usuarioLogado?.academia ?: ""
                        )
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = titulo.isNotBlank() && dataNaoNoFuturo(data.text)
                ) {
                    Text(text = "Salvar")
                }
            }
        }
    }
}
