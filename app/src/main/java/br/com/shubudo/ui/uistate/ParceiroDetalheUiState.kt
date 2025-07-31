import br.com.shubudo.model.Parceiro

data class ParceiroDetalheUiState(
    val isLoading: Boolean = false,
    val parceiro: Parceiro? = null,
    val error: String? = null
)