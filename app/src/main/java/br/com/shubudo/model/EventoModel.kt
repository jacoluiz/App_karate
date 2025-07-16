package br.com.shubudo.model

data class Evento(
    val _id: String,
    val titulo: String,
    val descricao: String,
    val dataInicio: String, // formato ISO
    val local: String
)
