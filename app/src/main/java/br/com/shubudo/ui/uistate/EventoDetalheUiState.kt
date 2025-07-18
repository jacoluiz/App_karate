import br.com.shubudo.model.Evento

data class EventoDetalheUiState(
    val isLoading: Boolean = false,
    val evento: Evento? = null,
    val error: String? = null
)