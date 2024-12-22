package br.com.shubudo.model

data class Projecao(
    val _id: String,
    val nome: String,
    val nomeJapones: String,
    val descricao: String,
    val observacao: List<String> = emptyList(),
    val ordem: Int,
    val faixa: String
)
