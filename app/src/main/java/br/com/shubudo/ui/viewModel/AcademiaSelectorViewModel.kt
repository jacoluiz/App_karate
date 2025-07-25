package br.com.shubudo.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.repositories.AcademiaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AcademiaSelectorViewModel @Inject constructor(
    private val repository: AcademiaRepository
) : ViewModel() {

    private val _filiais = mutableStateOf<List<String>>(emptyList())
    val filiais: State<List<String>> = _filiais

    init {
        carregarFiliais()
    }

    private fun carregarFiliais() {
        viewModelScope.launch {
            try {
                val academias = repository.getAcademias()
                val nomesFiliais: List<String> = academias
                    .first()
                    .flatMap { it.filiais }
                    .map { it.nome }
                    .distinct()


                _filiais.value = nomesFiliais.distinct() + "Outros"
            } catch (e: Exception) {
                _filiais.value = listOf("Outros")
            }
        }
    }
}

