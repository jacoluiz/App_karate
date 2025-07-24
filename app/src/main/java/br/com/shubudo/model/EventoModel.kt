package br.com.shubudo.model

data class Evento(
    val _id: String,
    val titulo: String,
    val descricao: String,
    val dataInicio: String,
    val local: String,
    val confirmados: List<String> = emptyList()
)