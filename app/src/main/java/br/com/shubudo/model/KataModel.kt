package br.com.shubudo.model

data class Kata(
    val _id: String,
    val faixa: String,
    val ordem: Int,
    val quantidadeMovimentos: Int,
    val movimentos: List<Movimento>
)
