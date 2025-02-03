package br.com.shubudo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.shubudo.ui.viewModel.DropDownMenuViewModel

@Composable
fun DropDownFaixa(
    titulo: String,
    dropDownMenuViewModel: DropDownMenuViewModel,
    onFaixaSelecionada: (String) -> Unit
) {
    val faixas = listOf("Branca", "Amarela", "Laranja", "Verde", "Roxa", "Marrom", "Preta")

    DropDownMenuSelect(
        titulo = titulo,
        viewmodel = dropDownMenuViewModel
    ) {
        Column(Modifier.padding(16.dp)) {
            faixas.forEach { faixa ->
                // Você pode usar um CustomIconButton ou Button
                Button(onClick = {
                    onFaixaSelecionada(faixa)
                    // Fecha o dropdown
                    dropDownMenuViewModel.changeExpanded(false)
                    dropDownMenuViewModel.changeSelected(true)
                }) {
                    Text(text = faixa)
                }
            }
        }
    }
}
