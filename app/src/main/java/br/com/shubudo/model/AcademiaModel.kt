package br.com.shubudo.model

data class Academia(
    val _id: String = "",
    val nome: String = "",
    val descricao: String = "",
    val filiais: List<Filial> = emptyList()
)

data class Filial(
    val _id: String = "",
    val nome: String = "",
    val endereco: String = "",
    val descricao: String = "",
    val imagens: List<String> = emptyList()
)