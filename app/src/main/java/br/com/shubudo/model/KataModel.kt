package br.com.shubudo.model

data class Kata(
    val id: String,
    val faixa: String,
    val ordem: Int,
    val quantidadeMovimentos: Int,
    val movimentos: List<Movimento>
)
