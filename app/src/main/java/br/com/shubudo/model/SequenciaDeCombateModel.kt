package br.com.shubudo.model

data class SequenciaDeCombate (
    val _id: String,
    val faixa: String,
    val numeroOrdem: Int,
    val movimentos: List<Movimento>,
    val video: String
)