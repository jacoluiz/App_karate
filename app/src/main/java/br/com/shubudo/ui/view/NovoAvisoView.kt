package br.com.shubudo.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.shubudo.model.Aviso
import br.com.shubudo.ui.viewModel.NovoAvisoViewModel
import kotlinx.coroutines.launch

@Composable
fun NovoAvisoView(
    // Callback para notificar a tela que o novo aviso foi salvo com sucesso.
    onSave: (Aviso) -> Unit,
    onCancel: () -> Unit,
    novoAvisoViewModel: NovoAvisoViewModel = hiltViewModel()
) {
    // Estados locais para os campos do formulário.
    var currentTitulo by remember { mutableStateOf("") }
    var currentConteudo by remember { mutableStateOf("") }
    var currentImagem by remember { mutableStateOf("") } // opcional

    // Estado para indicar se a operação de salvar está em andamento.
    var isSaving by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Campo para o Título do Aviso
        TextField(
            value = currentTitulo,
            onValueChange = { currentTitulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        // Campo para o Conteúdo do Aviso (multilinhas)
        TextField(
            value = currentConteudo,
            onValueChange = { currentConteudo = it },
            label = { Text("Conteúdo") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp), // altura ajustada para permitir várias linhas
            singleLine = false,
            maxLines = 10, // ou quantas linhas você achar necessário
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Default
            )
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
                            // Cria o objeto Aviso a ser salvo.
                            // (_id e dataCriacao podem ser preenchidos pelo backend.)
                            val novoAviso = Aviso(
                                _id = "",
                                titulo = currentTitulo,
                                conteudo = currentConteudo,
                                imagem = if (currentImagem.isBlank()) null else currentImagem,
                                arquivos = emptyList(),
                                ativo = true,
                                dataCriacao = "",
                                exclusivoParaFaixas = emptyList()
                            )
                            val avisoSalvo = novoAvisoViewModel.salvarAviso(novoAviso)
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
