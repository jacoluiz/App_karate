package br.com.shubudo.model

data class SequenciaDeCombate (
    val id: String,
    val faixa: String,
    val numeroOrdem: Int,
    val movimentos: List<Movimento>
)