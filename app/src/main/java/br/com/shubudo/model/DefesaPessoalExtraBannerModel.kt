package br.com.shubudo.model

data class DefesaPessoalExtraBanner(
    val _id: String,
    val faixa: String,
    val numeroOrdem: Int,
    val movimentos: List<Movimento>
)