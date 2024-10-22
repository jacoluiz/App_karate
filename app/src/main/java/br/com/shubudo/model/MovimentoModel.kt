package br.com.shubudo.model


data class Movimento (
    val _id: String,
    val faixa: String,
    val tipoMovimento: String,
    val base: String,
    val nome: String,
    val descricao: String,
    val ordem: Int,
    val observacao: List<String>
)