package br.com.shubudo.ui.viewModel.components

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

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        carregarFiliais()
    }

    private fun carregarFiliais() {
        viewModelScope.launch {
            _isLoading.value = true
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
            } finally {
                _isLoading.value = false
            }
        }
    }
}

