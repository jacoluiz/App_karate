package br.com.shubudo.model

data class Aviso(
    val id: String = "",
    val titulo: String = "",
    val conteudo: String = "",
    val dataHoraCriacao: String = "",
    val publicoAlvo: List<String> = emptyList(),
    val academia: String
)
