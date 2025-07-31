package br.com.shubudo.model

data class Parceiro(
    val _id: String? = null,
    val nome: String,
    val descricao: String,
    val localizacao: String? = null,
    val telefone: String,
    val site: String? = null,
    val logomarca: String,
    val imagens: List<String> = emptyList()
)
