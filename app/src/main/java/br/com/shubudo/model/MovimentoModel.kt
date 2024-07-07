package br.com.shubudo.model


data class Movimento (
    val id: String,
    val faixaCorresponde: String,
    val tipoMovimento: String,
    val base: String,
    val nome: String,
    val observacao: List<String>
)