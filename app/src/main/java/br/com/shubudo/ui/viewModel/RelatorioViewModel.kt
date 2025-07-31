package br.com.shubudo.ui.viewModel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shubudo.repositories.RelatorioRepository
import br.com.shubudo.ui.uistate.RelatorioUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RelatorioViewModel @Inject constructor(
    private val repository: RelatorioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RelatorioUiState>(RelatorioUiState.Idle)
    val uiState: StateFlow<RelatorioUiState> = _uiState.asStateFlow()

    /**
     * Baixa o relatório na API e salva em Downloads.
     * Emite estados: Downloading -> Success/Error.
     */
    fun baixarRelatorioOrganizado(
        context: Context,
        fileName: String = "relatorio-organizado.xlsx"
    ) {
        _uiState.value = RelatorioUiState.Downloading
        viewModelScope.launch {
            try {
                val uri = repository.baixarESalvarRelatorioOrganizado(context, fileName)
                _uiState.value = RelatorioUiState.Success(uri, fileName)
            } catch (e: Exception) {
                _uiState.value = RelatorioUiState.Error(e.message ?: "Erro ao baixar o relatório")
            }
        }
    }

    /** Opcional: volta para estado inicial (ex.: após Snackbar). */
    fun reset() {
        _uiState.value = RelatorioUiState.Idle
    }

    /**
     * (Opcional) Helper para abrir o arquivo baixado com algum app de planilha.
     * Use após receber Success. Trate ActivityNotFoundException na UI se quiser.
     */
    fun abrirRelatorio(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(
                uri,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
    }
}
