package br.com.shubudo.model

data class TecnicaChao(
    val _id: String,
    val nome: String,
    val descricao: String,
    val ordem: Int,
    val observacao: List<String>,
    val faixa: String,
    val video: String
)
