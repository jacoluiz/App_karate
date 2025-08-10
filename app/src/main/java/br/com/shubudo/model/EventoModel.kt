package br.com.shubudo.model

data class Presenca(
    val email: String,
    val confirmadoProfessor: Boolean = false,
    val academia: String,
    val cone: String = "",
    val fila: String = "",
    val chamada: String = ""
)

data class Evento(
    val _id: String,
    val titulo: String,
    val descricao: String,
    val dataInicio: String,
    val local: String,
    val confirmados: List<String> = emptyList(),
    val academia: String,
    val eventoOficial: Boolean = false,
    val presencas: List<Presenca> = emptyList()
)