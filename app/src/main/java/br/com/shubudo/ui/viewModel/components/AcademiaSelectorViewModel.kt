package br.com.shubudo.ui.viewModel.components

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.model.Academia
import br.com.shubudo.repositories.AcademiaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AcademiaSelectorViewModel @Inject constructor(
    private val repository: AcademiaRepository
) : ViewModel() {

    private val _academias = mutableStateOf<List<Academia>>(emptyList())
    val academias: State<List<Academia>> = _academias

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        carregarAcademias()
    }

    private fun carregarAcademias() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getAcademias().first()
                _academias.value = result
            } catch (e: Exception) {
                _academias.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

