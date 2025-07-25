import br.com.shubudo.model.Academia

sealed class AcademiaUiState {
    object Loading : AcademiaUiState()
    data class Success(val academias: List<Academia>) : AcademiaUiState()
    object Empty : AcademiaUiState()
    data class Error(val message: String) : AcademiaUiState()
}