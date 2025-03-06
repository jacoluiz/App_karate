package br.com.shubudo.model

data class Armamento (
    val _id: String,
    val arma: String,
    val faixa: String,
    val numeroOrdem: Int,
    val movimentos: List<Movimento>,
    val video: String
)