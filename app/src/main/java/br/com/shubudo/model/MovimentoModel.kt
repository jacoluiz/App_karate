package br.com.shubudo.model


data class Movimento (
    val _id: String,
    val faixa: String? = null,
    val tipoMovimento: String? = null,
    val base: String,
    val nome: String,
    val descricao: String,
    val ordem: Int,
    val observacao: List<String>
)