package br.com.shubudo.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class DropDownMenuViewModel : ViewModel() {
    private val _expanded = mutableStateOf(false)
    val expanded: State<Boolean> = _expanded

    private val _selected = mutableStateOf(false)
    val selected: State<Boolean> = _selected

    fun changeExpanded(expanded: Boolean) {
        _expanded.value = expanded
    }

    fun changeSelected(selected: Boolean) {
        _selected.value = selected
    }
}