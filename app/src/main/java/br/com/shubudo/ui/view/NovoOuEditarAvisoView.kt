package br.com.shubudo.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.model.Aviso
import br.com.shubudo.ui.viewModel.NovoAvisoViewModel
import kotlinx.coroutines.launch

@Composable
fun NovoOuEditarAvisoView(
    avisoId: String? = null, // Se for nulo, é uma criação; se não, estamos editando.
    onSave: (Aviso) -> Unit,
    onCancel: () -> Unit,
    novoAvisoViewModel: NovoAvisoViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    // Estado do aviso atual
    var aviso by remember { mutableStateOf<Aviso?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

    // Carregar aviso se estiver editando (tem um avisoId)
    LaunchedEffect(avisoId) {
        if (!avisoId.isNullOrBlank()) {
            isLoading = true
            aviso = novoAvisoViewModel.buscarAviso(avisoId)
            isLoading = false
        }
    }

    // Estados dos campos (inicializados com os valores do aviso quando carregado)
    var currentTitulo by remember { mutableStateOf(aviso?.titulo ?: "") }
    var currentConteudo by remember { mutableStateOf(aviso?.conteudo ?: "") }
    var currentImagem by remember { mutableStateOf(aviso?.imagem ?: "") }

    // Atualiza os campos quando o aviso for carregado
    LaunchedEffect(aviso) {
        aviso?.let {
            currentTitulo = it.titulo
            currentConteudo = it.conteudo
            currentImagem = it.imagem ?: ""
        }
    }

    val formTitle = if (avisoId == null) "Novo Aviso" else "Editar Aviso"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = formTitle,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Se estiver carregando o aviso, exibir loading
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Formulário de edição/criação do aviso
            TextField(
                value = currentTitulo,
                onValueChange = { currentTitulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                colors = TextFieldDefaults.colors()
            )

            TextField(
                value = currentConteudo,
                onValueChange = { currentConteudo = it },
                label = { Text("Conteúdo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                singleLine = false,
                maxLines = 10,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Default
                ),
                colors = TextFieldDefaults.colors()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botões de ação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onCancel) {
                    Text("Cancelar")
                }
                Button(
                    onClick = {
                        if (!isSaving) {
                            isSaving = true
                            coroutineScope.launch {
                                val avisoParaSalvar = Aviso(
                                    _id = aviso?._id ?: "",
                                    titulo = currentTitulo,
                                    conteudo = currentConteudo,
                                    imagem = if (currentImagem.isBlank()) null else currentImagem,
                                    arquivos = emptyList(),
                                    ativo = true,
                                    dataCriacao = aviso?.dataCriacao ?: "",
                                    exclusivoParaFaixas = aviso?.exclusivoParaFaixas ?: emptyList()
                                )

                                val avisoSalvo = if (aviso == null) {
                                    novoAvisoViewModel.salvarAviso(avisoParaSalvar)
                                } else {
                                    novoAvisoViewModel.atualizarAviso(avisoParaSalvar)
                                }

                                isSaving = false
                                if (avisoSalvo != null) {
                                    onSave(avisoSalvo)
                                }
                            }
                        }
                    }
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .height(24.dp)
                                .padding(end = 8.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}
