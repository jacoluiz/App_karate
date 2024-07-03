package br.com.shubudo.ui.view.module

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.shubudo.database.dao.FaixaDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModule @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val faixaDao: FaixaDao
): ViewModel(){
}